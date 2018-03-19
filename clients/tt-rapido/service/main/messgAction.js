var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");

var Messg_pb = require("../pb/IM.Message_pb.js");
var MsgData = Messg_pb["IMMsgData"];
var UnreadMsgCntRsp = Messg_pb["IMUnreadMsgCntRsp"];
var GetMsgListRsp = Messg_pb["IMGetMsgListRsp"];
var MsgDataAck = Messg_pb["IMMsgDataAck"];

var sessionAction = require("./sessionAction.js");
var buddyAction = require("./buddyAction.js");
var taryAction = require("./trayAction.js");
var groupAction = require("./groupAction.js");
var UserInfoAction = require("./userInfoAction.js");

var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var moment = require('moment');

var unreadMsgMap = new Map();
var sendMsgMap = new Map();

function getUnreadMsgJson(userId){
  return unreadMsgMap.get(userId);
}

function clearUnreadMsgJson(userId){
  return unreadMsgMap.delete(userId);
}

function ceckSendMsgStat(SeqNum){
  console.log("SeqNum:", SeqNum);
  if(sendMsgMap.get(SeqNum)){
    // TODO 消息发送失败时要做的事
  }
}

function sendMsg(msgJson){
  // TODO 消息为符合格式时的错误处理
  let msgDataObj = new MsgData();
  // TODO 添加未收到消息反馈时的处理；
  msgDataObj.setFromUserId(global.myUserId);
  msgDataObj.setToSessionId(msgJson.toSessionId);
  msgDataObj.setMsgId(0);
  msgDataObj.setCreateTime(moment().unix());
  msgDataObj.setMsgType(msgJson.MsgType);
  console.log(helper.encryption(new Buffer(msgJson.content)));
  let encrpMsg = new Buffer(helper.encryption(new Buffer(msgJson.content)));
  msgDataObj.setMsgData(new Uint8Array(encrpMsg));
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_MSG,
       BaseDefine_pb.MessageCmdID.CID_MSG_DATA);
  tcpClientModule.sendPacket(ttpbHeader, msgDataObj);

  sendMsgMap.set(ttpbHeader.getSeqNum(), msgJson);
  // 等待消息接收确认
  let timeoutObj = setTimeout(ceckSendMsgStat, 5000, ttpbHeader.getSeqNum());
}

function doMsgUnreadCntRsp(imPdu){
  let unread_msg_cntRsq = UnreadMsgCntRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  global.mainWindow.send('allMsgCnt-add', unread_msg_cntRsq.getTotalCnt());
  let unreadInfoPbList = unread_msg_cntRsq.getUnreadinfoListList();
  for(let item of unreadInfoPbList){
    let unreadMsgJson = {
      "toSessionId":item.getSessionId(),
      "toSessionType":item.getSessionType(),
      "unreadCnt":item.getUnreadCnt(),
      "msgId":item.getLatestMsgId(),
      "latestMsgData":item.getLatestMsgData(),
      "latestMsgType":item.getLatestMsgType(),
      "latestMsgFromUserId":item.getLatestMsgFromUserId()
    };
    unreadMsgMap.set(item.getSessionId() + "_" + item.getSessionType(), unreadMsgJson);
  }
  global.mainWindow.send('itemNewMsg-add', [...unreadMsgMap.values()]);
}

function doMsgListRsp(imPdu){
  let get_msg_list_Rsp = GetMsgListRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  // sessionAction.showMsg(get_msg_list_Rsp.getSessionType(),
  //         get_msg_list_Rsp.getSessionId(), get_msg_list_Rsp.getMsgListList());
  helper.doCallback(imPdu.getSeqNum(), get_msg_list_Rsp.getSessionType(),
          get_msg_list_Rsp.getSessionId(), get_msg_list_Rsp.getMsgListList());
}

function doMsgData(imPdu){
  let msgDataObj = MsgData.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let sessionType, SessionId;
  switch (msgDataObj.getMsgType()) {
    case BaseDefine_pb.MsgType.MSG_TYPE_SINGLE_TEXT:
      sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE;
      SessionId = msgDataObj.getFromUserId();
      break;
    case BaseDefine_pb.MsgType.MSG_TYPE_SINGLE_AUDIO:
      sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE;
      SessionId = msgDataObj.getFromUserId();
      break;
    case BaseDefine_pb.MsgType.MSG_TYPE_GROUP_TEXT:
      sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_GROUP;
      SessionId = msgDataObj.getToSessionId();
      break;
    case BBaseDefine_pb.MsgType.MSG_TYPE_GROUP_AUDIO:
      sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_GROUP;
      SessionId = msgDataObj.getToSessionId();
      break;
    default:
      console.log("获取消息类型错误:", msgDataObj.getMsgType());
      return;
  }
  let localSessionId = SessionId + "_" + sessionType;
  //console.log("localSessionId", localSessionId);

  let unreadMsgJson = {
    toSessionId:SessionId,
    toSessionType:sessionType,
    msgId:msgDataObj.getMsgId(),
    msgData:helper.u8arrToDecry(msgDataObj.getMsgData()),
    msgTime:helper.getDateStr(msgDataObj.getCreateTime()),
    msgType:msgDataObj.getMsgType(),
    fromUserId:msgDataObj.getFromUserId()
  };

  buddyAction.setLatestMsgId(SessionId, sessionType, msgDataObj.getMsgId());
  if (sessionAction.hasWindow(localSessionId)){
    sessionAction.showMsg(sessionType ,SessionId, [msgDataObj]);
    global.mainWindow.send('itemNewMsg-update', unreadMsgJson);
  } else{
    global.mainWindow.send('allMsgCnt-add', 1);
    if(unreadMsgMap.get(localSessionId)){
      unreadMsgJson.unreadCnt = unreadMsgMap.get(localSessionId).unreadCnt+ 1;
    } else {
      unreadMsgJson.unreadCnt = 1;
    }
    unreadMsgMap.set(localSessionId, unreadMsgJson);
    global.mainWindow.send('itemNewMsg-add', [unreadMsgJson]);

    // TODO 增加使用对方的头像闪烁；
    // 系统托盘闪烁
    if (!global.mainWindow.isFocused()){
      taryAction.startTray(1);
    }
  }

  if(sessionType == BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE){
    UserInfoAction.getUserInf_Online([unreadMsgJson.toSessionId], global.mainWindow);
  }else if(sessionType == BaseDefine_pb.SessionType.SESSION_TYPE_GROUP){
    let groupInfo = groupAction.getGroupInfo(unreadMsgJson.toSessionId)
    global.mainWindow.send('groupinfo-add', groupInfo);
  }
}

function doMsgDataAck(imPdu){
  let msgDataAckObj = MsgDataAck.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let seqNum = imPdu.getSeqNum();
  // console.log("delte squm:", seqNum);
  if(sendMsgMap.has(seqNum)){
    let objJson = sendMsgMap.get(seqNum);
    let myMsgJson = {
      toSessionId: msgDataAckObj.getSessionId(),
      toSessionType: msgDataAckObj.getSessionType(),
      msgId:msgDataAckObj.getMsgId(),
      msgData:objJson.content,
      msgTime:helper.getDateStr(moment().unix()),
      msgType:objJson.MsgType,
      fromUserId:global.myUserId,
    };

    sessionWin = sessionAction.hasWindow( msgDataAckObj.getSessionId() + "_"
      +  msgDataAckObj.getSessionType());
    if(sessionWin){
      // console.log("myMsgJson:",myMsgJson);
      sessionWin.send('msg-show', [myMsgJson]);
    }
    // if(BaseDefine_pb.MsgType.MSG_TYPE_GROUP_TEXT == objJson.MsgType||
    // BaseDefine_pb.SessionType.MSG_TYPE_GROUP_AUDIO == objJson.MsgType){
    //   myMsgJson.sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_GROUP;
    // } else if(BaseDefine_pb.MsgType.MSG_TYPE_SINGLE_TEXT == objJson.MsgType||
    // BaseDefine_pb.SessionType.MSG_TYPE_SINGLE_AUDIO == objJson.MsgType) {
    //   myMsgJson.sessionType = BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE;
    // } else{
    //   // TODO do something!!!
    //   console.log("wrong msg typye!!!!");
    // }
    global.mainWindow.send('itemNewMsg-update', myMsgJson);
    if(myMsgJson.toSessionType == BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE){
      UserInfoAction.getUserInf_Online([myMsgJson.toSessionId], global.mainWindow);
    }else if(myMsgJson.toSessionType == BaseDefine_pb.SessionType.SESSION_TYPE_GROUP){
      let groupInfo = groupAction.getGroupInfo(myMsgJson.toSessionId)
      global.mainWindow.send('groupinfo-add', groupInfo);
    } else{
      console.log("can't get myMsgJson.sessionType!!! ");
    }
  }
  sendMsgMap.delete(seqNum);
}

function messgRoute(imPdu){
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.MessageCmdID.CID_MSG_DATA:
      doMsgData(imPdu);
      break;
    case BaseDefine_pb.MessageCmdID.CID_MSG_DATA_ACK:
      doMsgDataAck(imPdu);
      break;
    case BaseDefine_pb.MessageCmdID.CID_MSG_UNREAD_CNT_RESPONSE:
      doMsgUnreadCntRsp(imPdu);
      break;
    case BaseDefine_pb.MessageCmdID.CID_MSG_LIST_RESPONSE:
      doMsgListRsp(imPdu);
      break;
    default:
      console.log("BaseDefine_pb.MessageCmdID, 命令ＩＤ没有处理:", imPdu.getCmdid());
  }
}

exports.clearUnreadMsgJson = clearUnreadMsgJson;
exports.getUnreadMsgJson = getUnreadMsgJson;
exports.sendMsg = sendMsg;
exports.messgRoute = messgRoute;
