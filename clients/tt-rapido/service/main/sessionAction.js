const BrowserWindow = require('electron').BrowserWindow;
var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");

var Message_pb = require("../pb/IM.Message_pb.js");
var GetMsgListReq = Message_pb['IMGetMsgListReq'];
var MsgDataReadAck = Message_pb['IMMsgDataReadAck'];
var MsgData = Message_pb["IMMsgData"];

var buddyAction = require("./buddyAction.js");
var messgAction = require("./messgAction.js");
var groupAction = require("./groupAction.js");
var taryAction = require("./trayAction.js");
var UserInfoAction = require("./userInfoAction.js");

var helper = require("../assistant/helper.js");
var config = require("../assistant/config.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var moment = require('moment');
let SessObjMap = new Map();

function hasWindow(sessLocalId){
  return SessObjMap.get(sessLocalId);
}

function getDateStr(datetimeUnix){
  let dateObj = moment.unix(datetimeUnix)
  return dateObj.format("GGGG-M-D H:mm:ss");
}

function msgList2msgJsonList(msgList){
  let msgJsonList = []
  for (let item of msgList){
    let msgJson = {
      "msgId":item.getMsgId(),
      "msgTime":getDateStr(item.getCreateTime()),
      "msgType":item.getMsgType(),
      "msgData":helper.u8arrToDecry(item.getMsgData()),
    }
    if (item instanceof MsgData){
      msgJson.fromUserId = item.getFromUserId();
    } else{
      msgJson.fromUserId = item.getFromSessionId();
    }
    msgJsonList.push(msgJson);
  }
  return msgJsonList;
}

function showMsg(sessionType, sessionId, msgList){
  let sessLocalId = sessionId + "_" + sessionType;
  let msgJsonList = msgList2msgJsonList(msgList);

  SessObjMap.get(sessLocalId).send('msg-show',msgJsonList);
  let msgDataReadAckObj = new MsgDataReadAck();
  msgDataReadAckObj.setUserId(global.myUserId);
  msgDataReadAckObj.setSessionId(sessionId);
  msgDataReadAckObj.setMsgId("1");
  msgDataReadAckObj.setSessionType(sessionType);

  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_MSG,
      BaseDefine_pb.MessageCmdID.CID_MSG_READ_ACK);
  console.log("send msg read ack");
  tcpClientModule.sendPacket(ttpbHeader, msgDataReadAckObj);

  if(!SessObjMap.get(sessLocalId).isFocused()){
    SessObjMap.get(sessLocalId).flashFrame(true);
  }
}

function showHistoryMsg(sessionType, sessionId, msgList){
  let sessLocalId = sessionId + "_" + sessionType;
  let msgJsonList = msgList2msgJsonList(msgList);

  if(SessObjMap.get(sessLocalId)){
    SessObjMap.get(sessLocalId).send('msg-history',msgJsonList);
  } else {
    console.log("Can't get session!!!", sessLocalId);
  }
}


function openSession(sessionId, sessionType){
  let myUserId = global.myUserId;
  //打开除了自己以外其他openSession窗口
  if(sessionId != myUserId){
    // console.log("openSession:", sessionId, sessionType);
    let sessLocalId = sessionId + "_" + sessionType;
    let creatingSession = false;
    let openWindow = SessObjMap.get(sessLocalId);
    if(openWindow){
      openWindow.show();
    }else if (creatingSession){
      console.log("正在打开对话页面");
    }else{
      creatingSession = true;
      openWindow = creatNewSession(sessionId, sessionType);
      creatingSession = false;
    }
    return openWindow;
  }
}

async function creatNewSession(sessionId, sessionType){
  let sessLocalId = sessionId + "_" + sessionType;
  let objInfoJson = {};
  let sessionWinJson = {};
  switch (sessionType) {
    case BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE:
      //objInfoJson = UserInfoAction.getUserInfo(sessionId);
      let objInfoAsynResult = await UserInfoAction.getUserInfo(sessionId);
      objInfoJson = objInfoAsynResult
      // 创建个人对话窗口
      sessionWinJson = {
        width: config.sessWinWidth,
        height: config.sessWinHeight,
        title: objInfoJson.nickName,
        frame:false,
        transparent:true
      };
      break;
    case BaseDefine_pb.SessionType.SESSION_TYPE_GROUP:
      objInfoJson = groupAction.getGroupInfo(sessionId);
      // 创建讨论组、群对话窗口
      sessionWinJson = {
        width: config.sessWinWidth + 200,
        height: config.sessWinHeight,
        title: objInfoJson,
        frame:false,
        transparent:true
      };
      break;
    default:
      console.log("openSession something wrong sessAction !!!!");
  }
  objInfoJson.toSessionId = sessionId;
  objInfoJson.toSessionType = sessionType;
  let sessionJson = buddyAction.getSessJson(sessionId, sessionType);
  debugger;
  if(sessionJson){
    objInfoJson.msgId = sessionJson.msgId;
  }
  // 停止闪烁；
  taryAction.stopTray("1");
  // 创建对话窗口

  let sessionWindow = new BrowserWindow(sessionWinJson);
  sessionWindow.loadURL('file://' +  global.maindir + '/main/session.html');

  // Debug mode
  if (config.isDev){
    sessionWindow.webContents.openDevTools();
  }

  sessionWindow.on('closed', function () {
    SessObjMap.delete(sessLocalId);
    console.log("sessionWindow: closed!!!");
  })

  sessionWindow.webContents.on('did-finish-load', function(){
    console.log("openssesion success!!!");
    // 加载对话信息；
    //let objInfo = buddyAction.getUserInfo(sessionId);
    sessionWindow.send('objInfo-load',objInfoJson,global.myUserId);
    sessionWindow.send('myInfo-load', UserInfoAction.getUserInfo(global.myUserId));
    if (BaseDefine_pb.SessionType.SESSION_TYPE_GROUP == sessionType){
      // objInfoJson.groupMemberListList
      console.info('loading user list:')
      console.info(objInfoJson)
      UserInfoAction.loadUserInfo(objInfoJson.groupMemberListList, function(userInfoList){
        sessionWindow.send('gmember-load', userInfoList);
        getUnreadMsg(sessLocalId);
      });
    } else{
        getUnreadMsg(sessLocalId);
    }
    SessObjMap.set(sessLocalId, sessionWindow);

    sessionWindow.setMinimumSize(config.sessWinWidth,config.sessWinHeight); //设置窗口最小化的宽高值

    // 更新未读消息数
    global.mainWindow.send("itemNewMsg-clear", objInfoJson);
    // global.mainWindow.send("allMsgCnt-sub", unreadMsgJson.unreadCnt);
    return sessionWindow;
  })
}

// 获取未读消息；
function getUnreadMsg(sessLocalId){
  let unreadMsgJson = messgAction.getUnreadMsgJson(sessLocalId);
  if (unreadMsgJson){
    getMsg(unreadMsgJson.toSessionId, unreadMsgJson.toSessionType,
      unreadMsgJson.msgId, unreadMsgJson.unreadCnt,
      function(sessionType, sessionId, msgList){
        showMsg(sessionType, sessionId, msgList);
        // TODO 添加获取历史消息的msgId更新
      });
    messgAction.clearUnreadMsgJson(sessLocalId);
  }
}

// 获取历史消息；
function getHistoryMsg(sessionId, sessionType, latestMsgId){
  // getMsg(sessionId, sessionType, latestMsgId, 11,
  //   function(sessionType, sessionId, msgList){
  //     showMsg(sessionType, sessionId, msgList, true);
  //   });
  getMsg(sessionId, sessionType, latestMsgId, 11, showHistoryMsg);
}

function getMsg(sessionId, sessionType, latestMsgId, unreadCnt, callback){
  let getMsgListReqObj = new GetMsgListReq();
  getMsgListReqObj.setUserId(global.myUserId);
  getMsgListReqObj.setSessionType(sessionType);
  getMsgListReqObj.setSessionId(sessionId);
  getMsgListReqObj.setMsgIdBegin(latestMsgId?latestMsgId: 0);
  getMsgListReqObj.setMsgCnt(unreadCnt);

  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_MSG,
      BaseDefine_pb.MessageCmdID.CID_MSG_LIST_REQUEST);
  tcpClientModule.sendPacket(ttpbHeader, getMsgListReqObj);
  console.log(sessionId, sessionType, latestMsgId, unreadCnt);
  helper.addCallback(ttpbHeader.getSeqNum(), callback);
}

function closeWindow(sessionId, sessionType){
  let sessLocalId = sessionId + "_" + sessionType;
  if (SessObjMap.get(sessLocalId)){
    SessObjMap.get(sessLocalId).close();
  }
}

function maxWindow(sessionId, sessionType){
  let sessLocalId = sessionId + "_" + sessionType;
  if (SessObjMap.get(sessLocalId)){
    let winObj = SessObjMap.get(sessLocalId);
    if(winObj.isMaximized()){
      winObj.unmaximize();
    } else{
      winObj.maximize();
    }
  }
}

function miniWindow(sessionId, sessionType){
  let sessLocalId = sessionId + "_" + sessionType;
  if (SessObjMap.get(sessLocalId)){
    SessObjMap.get(sessLocalId).minimize();
  }
}

exports.hasWindow = hasWindow;
exports.showMsg = showMsg;
exports.openSession = openSession;
exports.getHistoryMsg = getHistoryMsg;
exports.closeWindow = closeWindow;
exports.maxWindow = maxWindow;
exports.miniWindow = miniWindow;
exports.SessObjMap = SessObjMap;
