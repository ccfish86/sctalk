const fs = require('fs');
const FILE_TRANSFER_BLOCK_SIZE = 1024 * 34;//每一块的大小

var moment = require('moment');
var path = require('path');

var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");
var File_pb = require("../pb/IM.File_pb.js");
var FileReq = File_pb["IMFileReq"];
var FileRsp = File_pb["IMFileRsp"];
var FileLoginReq = File_pb["IMFileLoginReq"];
var FileLoginRsp = File_pb["IMFileLoginRsp"];
var FileNotify = File_pb["IMFileNotify"];
var FileState = File_pb["IMFileState"];
var FilePullDataReq = File_pb["IMFilePullDataReq"];
var FilePullDataRsp = File_pb["IMFilePullDataRsp"];

var UserInfoAction = require("./userInfoAction.js");
var sessionAction = require("./sessionAction.js");

var CImPdu = require('../assistant/CImPdu.js');
var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var connServer = require('../assistant/connServer.js');

var fileMap = new Map();
var socktMap = new Map();
var fileNotifyMap = new Map();

function fileServerLogin(taskId, fileRole, ipaddr, port, callback){
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_FILE,
      BaseDefine_pb.FileCmdID.CID_FILE_LOGIN_REQ);
  let fileLoginReqObj = new FileLoginReq();
  fileLoginReqObj.setUserId(global.myUserId);
  fileLoginReqObj.setTaskId(taskId);
  fileLoginReqObj.setFileRole(fileRole);

  let fileSocketConn = new connServer(ipaddr, port, fileSocketRoute);
  fileSocketConn.conn();
  fileSocketConn.senddata(helper.getSendBuf(ttpbHeader,fileLoginReqObj));
  socktMap.set(taskId, fileSocketConn);
  helper.addCallback(ttpbHeader.getSeqNum(), callback);
  console.log("get fileRsq, connect to fileServer", ipaddr, port);
}

function sendFileReq(fileObj){
  console.log("fileObj:", fileObj);
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_FILE,
      BaseDefine_pb.FileCmdID.CID_FILE_REQUEST);
  let FileReqObj = new FileReq();
  FileReqObj.setFromUserId(global.myUserId);
  FileReqObj.setToUserId(fileObj.toObjUserId);
  FileReqObj.setFileName(fileObj.fileName);
  FileReqObj.setFileSize(fileObj.fileSize);
  let toUserStat = UserInfoAction.getUserStat(fileObj.toObjUserId);
  if(BaseDefine_pb.UserStatType.USER_STATUS_OFFLINE == toUserStat){
    FileReqObj.setTransMode(BaseDefine_pb.TransferFileType.FILE_TYPE_OFFLINE);
  } else{
    FileReqObj.setTransMode(BaseDefine_pb.TransferFileType.FILE_TYPE_ONLINE);
  }
  tcpClientModule.sendPacket(ttpbHeader, FileReqObj);
  helper.addCallback(ttpbHeader.getSeqNum(), function(fileRspObj){
      fileObj.sendTime = moment().unix();
      fileMap.set(fileRspObj.getTaskId(), fileObj);
      console.log(fileRspObj.getTaskId(), fileMap);
      let fileRole = BaseDefine_pb.TransferFileType.FILE_TYPE_ONLINE
        == fileRspObj.getTransMode()?
        BaseDefine_pb.ClientFileRole.CLIENT_REALTIME_SENDER
        : BaseDefine_pb.ClientFileRole.CLIENT_OFFLINE_UPLOAD;
      fileServerLogin(fileRspObj.getTaskId(), fileRole,
        fileRspObj.getIpAddrListList()[0].getIp(),
        fileRspObj.getIpAddrListList()[0].getPort() , sendFileLoginSucc)
  })
}

function doFileRsp(imPdu){
  let fileRspObj = FileRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  if (!fileRspObj.getResultCode()){
    helper.doCallback(imPdu.getSeqNum(), fileRspObj)
    console.log("doFileRsp: get task_id:", fileRspObj.getTaskId());
  } else{
    console.log("Can't connet to File Server!!!");
  }
}

function doFileNotify(imPdu){
  let fileNotifyObj = FileNotify.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  console.log("fileNotifyObj:", fileNotifyObj.getFromUserId(), fileNotifyObj.getToUserId(),
    fileNotifyObj.getFileName(), fileNotifyObj.getFileSize(), fileNotifyObj.getTaskId());
  fileReq = {
    task_id : fileNotifyObj.getTaskId(),
    fileName :  path.basename(fileNotifyObj.getFileName()),
    fileSize : fileNotifyObj.getFileSize(),
    filetime : "11111",
  }
  let sessWin = sessionAction.hasWindow(fileNotifyObj.getFromUserId() + "_"
    + BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
  if(!sessWin){
    sessionAction.openSession(fileNotifyObj.getFromUserId()
      , BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
    // TODO 以后再完善下为打开窗口是文件传输的情况
    setTimeout(function() {
      let sessWin = sessionAction.hasWindow(fileNotifyObj.getFromUserId() + "_"
        + BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
      sessWin.send('resvFile-notif', fileReq);
    }, 1000);
  } else{
    sessWin.send('resvFile-notif', fileReq);
  }
  fileNotifyMap.set(fileNotifyObj.getTaskId(), fileNotifyObj);
}

function fileRoute(imPdu){
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.FileCmdID.CID_FILE_RESPONSE:
      doFileRsp(imPdu);
      break;
    case BaseDefine_pb.FileCmdID.CID_FILE_NOTIFY:
      doFileNotify(imPdu);
      break;
    default:
      console.log("BaseDefine_pb.FileCmdID, 命令id没有处理:", imPdu.getCmdid());
  }
}

function accecptFile(taskId){
  let fileNotify = fileNotifyMap.get(taskId);
  let fileRole = BaseDefine_pb.TransferFileType.FILE_TYPE_ONLINE == fileNotify.getTransMode() ? BaseDefine_pb.ClientFileRole.CLIENT_REALTIME_RECVER : BaseDefine_pb.ClientFileRole.CLIENT_REALTIME_RECVER;
  fileServerLogin(taskId, fileRole,fileNotify.getIpAddrListList()[0].getIp(),fileNotify.getIpAddrListList()[0].getPort(),getFileLoginSucc);

  let fileName = fileNotify.getFileName();
  //文件默认存在位置处理
  let filePath = global.files;
  fs.exists(filePath, (exists) => {
    if(exists){
      console.log(filePath);
    }else{
      // TODO 默认文件夹不存在，创建该文件夹
      fs.mkdir(filePath, function(err){
        if(err){  
          throw err;  
        }  
        console.log('make dir success.');
      });  
    }
  });
  let fileObj = {
    fileName : path.basename(fileName),
    filePath : filePath,
    fileSize : fileNotify.getFileSize(),
    toObjUserId : fileNotify.getFromUserId(),
  }
  console.log(filePath);
  fileMap.set(taskId, fileObj);
  fileNotifyMap.delete(taskId);
}

function sendFileLoginSucc(taskId){
  fileObj = fileMap.get(taskId);
  let sessWin = sessionAction.hasWindow(fileObj.toObjUserId + "_"
    + BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
  if(sessWin){
    fileObj.task_id = taskId;
    fileObj.percent = 0;
    fileObj.filetime = helper.getDateStr(fileObj.sendTime);
    sessWin.send('sendFile-req', fileObj);
  }
}

function getFileLoginSucc(taskId, offset = 0){
  console.log("getFileLoginSucc:", taskId, offset);
  let fileObj = fileMap.get(taskId);
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_FILE,
      BaseDefine_pb.FileCmdID.CID_FILE_PULL_DATA_REQ);
  let filePullDataReqObj = new FilePullDataReq();
  filePullDataReqObj.setTaskId(taskId);
  filePullDataReqObj.setUserId(global.myUserId);
  filePullDataReqObj.setTransMode(BaseDefine_pb.ClientFileRole.CLIENT_REALTIME_RECVER);
  filePullDataReqObj.setOffset(offset);
  let lastSize = fileObj.fileSize - offset;
  let datasize = lastSize< FILE_TRANSFER_BLOCK_SIZE
    ? lastSize : FILE_TRANSFER_BLOCK_SIZE;

  filePullDataReqObj.setDataSize(datasize);
  if(socktMap.get(taskId)){
    socktMap.get(taskId).senddata(helper.getSendBuf(ttpbHeader,filePullDataReqObj));
  }else{
    console.log("error connection!!!");
  }
}

function doFileLoginRsp(imPdu){
  let fileLoginRspObj = FileLoginRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  if(!fileLoginRspObj.getResultCode()){
    helper.doCallback(imPdu.getSeqNum(), fileLoginRspObj.getTaskId())
  }else{
    console.log("Can't connet login to File server!!!");
  }
}

function releaseTask(task_id){
  fileMap.delete(task_id);
  let socketObj = socktMap.get(task_id);
  if (socketObj){
    console.log(socketObj);
    socketObj.close();
  }
  socktMap.delete(task_id);
}

function doFileStateRsp(imPdu){
  let fileStateObj = FileState.deserializeBinary(new Uint8Array(imPdu.getPbBody()))
  console.log(fileStateObj.getTaskId(),fileStateObj.getState());
  fileObj = fileMap.get(fileStateObj.getTaskId());
  let resultObj = {
    result : fileStateObj.getState(),
    task_id : fileStateObj.getTaskId(),
    fileName : fileObj.fileName,
  }
  if (fileStateObj.getState() == BaseDefine_pb.ClientFileState.CLIENT_FILE_DONE){
    console.log("send file success!!!!!", fileStateObj.getTaskId());
    resultObj.content = '"'+fileObj.fileName+'" 发送成功！';
  }
  if (fileStateObj.getState() == BaseDefine_pb.ClientFileState.CLIENT_FILE_REFUSE){
    console.log("send file refulse!!!!!", fileStateObj.getTaskId());
    resultObj.content = '对方拒绝发送文件："'+fileObj.fileName+'"！';
  }
  if (fileStateObj.getState() == BaseDefine_pb.ClientFileState.CLIENT_FILE_CANCEL){
    console.log("send file canel!!!!!", fileStateObj.getTaskId());
    resultObj.content = '对方取消发送文件："'+fileObj.fileName+'"！';
  }
  let sessWin = sessionAction.hasWindow(fileStateObj.getUserId() + "_"
    +BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
  sessWin.send('sendFile-result', resultObj);
}

function doFilePullDataReq(imPdu){
  let filePullDataReqObj = FilePullDataReq.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let filesize = filePullDataReqObj.getDataSize();
  let fileOffset = filePullDataReqObj.getOffset();
  let taskId = filePullDataReqObj.getTaskId();
  let fileObj = fileMap.get(taskId);

  fs.open(fileObj.filePath, 'r', function (err, fd) {
    if(!err) {
      let buffer = new Buffer(filesize);
      fs.read(fd, buffer, 0, filesize, fileOffset, function (err, bytesRead, buffer) {
        if(!err) {
          let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_FILE,
              BaseDefine_pb.FileCmdID.CID_FILE_PULL_DATA_RSP);
          let filePullDataRspObj = new FilePullDataRsp();
          filePullDataRspObj.setResultCode(0);
          filePullDataRspObj.setTaskId(taskId);
          filePullDataRspObj.setUserId(global.myUserId);
          filePullDataRspObj.setOffset(fileOffset);
          filePullDataRspObj.setFileData(new Uint8Array(buffer));
          if(socktMap.get(taskId)){
            socktMap.get(taskId).senddata(helper.getSendBuf(ttpbHeader,filePullDataRspObj));
          }else{
            console.log("error connection!!!");
          }
        } else {
          throw err;
        }
      });
    } else {
      console.error(err);
      return;
    }

    let sessWin = sessionAction.hasWindow(fileObj.toObjUserId + "_"
      +BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
    if(sessWin){
      fileObj.task_id = taskId;
      fileObj.percent = (Math.floor((fileOffset+filesize)*100/fileObj.fileSize));
      sessWin.send('sendFile-req', fileObj);
    }
  });
}

function doFilePullDataRsp(imPdu){
  let filePullDataRspObj = FilePullDataRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let fileOffset = filePullDataRspObj.getOffset();
  let taskId = filePullDataRspObj.getTaskId();
  let fileData = filePullDataRspObj.getFileData();
  let fileObj = fileMap.get(taskId);

  fs.open(fileObj.filePath + '/' + fileObj.fileName, 'a', function (err, fd) {
    if(!err) {
      fs.write(fd, new Buffer(fileData), 0, fileData.length, fileOffset, function (err) {
        if(!err) {
          if (fileObj.fileSize > fileOffset+fileData.length){
            getFileLoginSucc(taskId, fileOffset+fileData.length);
          }
        } else {
          console.error(err);
          return;
        }
      });
    } else {
      console.error(err);
      return;
    }
  });
  let sessWin = sessionAction.hasWindow(fileObj.toObjUserId + "_"
    +BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE);
  if(sessWin){
    fileObj.task_id = taskId;
    fileObj.percent = (Math.floor((fileOffset+fileData.length)*100/fileObj.fileSize));
    sessWin.send('getFile-req', fileObj);
  }
}

function fileSocketRoute(data){
  var imPdu = new CImPdu(data);
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.FileCmdID.CID_FILE_LOGIN_RES:
      doFileLoginRsp(imPdu);
      break;
    case BaseDefine_pb.FileCmdID.CID_FILE_STATE:
      doFileStateRsp(imPdu);
      break;
    case BaseDefine_pb.FileCmdID.CID_FILE_PULL_DATA_REQ:
      doFilePullDataReq(imPdu);
      break;
    case BaseDefine_pb.FileCmdID.CID_FILE_PULL_DATA_RSP:
      doFilePullDataRsp(imPdu);
      break;
    default:
      console.log("fileSocketRoute, 命令id没有处理:", imPdu.getCmdid());
  }
}

exports.sendFileReq = sendFileReq;
exports.accecptFile = accecptFile;
exports.fileRoute = fileRoute;
