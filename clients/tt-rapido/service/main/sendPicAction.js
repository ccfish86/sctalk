var exec = require('child_process').exec;
const {clipboard} = require('electron');

var helper = require("../assistant/helper.js");
var postPicture = require("../assistant/postPicture.js");
var messgAction = require("./messgAction.js");
var constant = require('../assistant/constant.js');

const nativeImage = require('electron').nativeImage
var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");

function doPostPicRsp(rspData, fileObj){
  let dataJson = JSON.parse(rspData);
  if(dataJson.code == 0){
    console.log("rspdata:", dataJson.url, fileObj.toSessionType);

    let imgUrl = "&$#@~^@[{:"+dataJson.url + ":}]&$~@#@";
    let msgType;

    let msgJson = {
      toSessionId : fileObj.toSessionId,
      content : imgUrl,
    }
    if(BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE == fileObj.toSessionType){
      msgJson.MsgType = BaseDefine_pb.MsgType.MSG_TYPE_SINGLE_TEXT;
    } else{
      msgJson.MsgType = BaseDefine_pb.MsgType.MSG_TYPE_GROUP_TEXT;
    }
    messgAction.sendMsg(msgJson);
  } else{
    // TODO 发送图片失败 Do something!!!
    console.warn('doPostPicRsp faild')
  }
}

function sendPicReq(fileObj){
  postPicture.postPicture(fileObj.picPath,function(data){
    doPostPicRsp(data, fileObj);
  });
}

function screenShot(toSessionId, toSessionType){
  clipboard.clear();
  exec(global.maindir + constant.SCREENER_DIR, function(error, stdout, stderr){
    if(!error){
      let imgObj = clipboard.readImage();
      if(!imgObj.isEmpty()){
        postPicture.postByNativeImg(imgObj, function(data){
          fileObj = {
            toSessionId: toSessionId,
            toSessionType:toSessionType
          }
          doPostPicRsp(data, fileObj);
        });
      }
    }
  });
}

exports.screenShot = screenShot;
exports.sendPicReq = sendPicReq;
