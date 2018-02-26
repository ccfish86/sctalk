var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");
var Buddy_pb = require("../pb/IM.Buddy_pb.js");
var BuddyListRsp = Buddy_pb["IMRecentContactSessionRsp"];
var BuddyListReq = Buddy_pb['IMRecentContactSessionReq'];
var DepartmentReq = Buddy_pb['IMDepartmentReq'];
var DepartmentRsp = Buddy_pb['IMDepartmentRsp'];

var Messg_pb = require("../pb/IM.Message_pb.js");
var UnreadMsgCntReq = Messg_pb["IMUnreadMsgCntReq"];

var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');

var UserInfoAction = require("./userInfoAction.js");
var groupAction = require('./groupAction.js');

var userSessionMap = new Map();
var groupSessionMap = new Map();

function getSessJson(sessionId, sessionType) {
  if (BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE == sessionType) {
    return userSessionMap.get(sessionId);
  } else if (BaseDefine_pb.SessionType.SESSION_TYPE_GROUP == sessionType) {
    return groupSessionMap.get(sessionId);
  }
}

function setLatestMsgId(sessionId, sessionType, msgId) {
  let sessJson = {
    "toSessionId": sessionId,
    "toSessionType": sessionType,
    "msgId": msgId,
  }
  if (BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE == sessionType) {
    if (userSessionMap.get(sessionId)) {
      userSessionMap.get(sessionId).msgId = msgId;
    } else {
      userSessionMap.set(sessionId, sessJson);
    }
  } else if (BaseDefine_pb.SessionType.SESSION_TYPE_GROUP == sessionType) {
    if (groupSessionMap.get(sessionId)) {
      groupSessionMap.get(sessionId).msgId = msgId;
    } else {
      groupSessionMap.set(sessionId, sessJson);
      // TODO 拉去新建的组群信息
    }
  }
}

function getRecentSession() {
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST,
    BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST);
  let buddy_list = new BuddyListReq();
  buddy_list.setUserId(global.myUserId);
  buddy_list.setLatestUpdateTime(0);
  tcpClientModule.sendPacket(ttpbHeader, buddy_list);
}

function doBuddyListRsp(imPdu) {
  let buddyListRsp = BuddyListRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let buddyList = buddyListRsp.getContactSessionListList();
  let sessinfoList = [];
  for (let item of buddyList) {
    let sessJson = {
      "toSessionId": item.getSessionId(),
      "toSessionType": item.getSessionType(),
      "fromUserId": item.getLatestMsgFromUserId(),
      "msgData": helper.u8arrToDecry(item.getLatestMsgData()),
      "msgTime": helper.getDateStr(item.getUpdatedTime()),
      "msgId": item.getLatestMsgId(),
    }
    sessinfoList.push(sessJson);
    if (item.getSessionType() == BaseDefine_pb.SessionType.SESSION_TYPE_SINGLE) {
      userSessionMap.set(item.getSessionId(), sessJson);
    } else if (item.getSessionType() == BaseDefine_pb.SessionType.SESSION_TYPE_GROUP) {
      groupSessionMap.set(item.getSessionId(), sessJson);
    }
  }

  global.mainWindow.send('session-reply', sessinfoList);
  UserInfoAction.getUserInf_Online([...userSessionMap.keys()], global.mainWindow);
  // 获取离线消息;
  setTimeout(function () {
    let unread_msg_cntReq = new UnreadMsgCntReq();
    unread_msg_cntReq.setUserId(global.myUserId);

    let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_MSG,
      BaseDefine_pb.MessageCmdID.CID_MSG_UNREAD_CNT_REQUEST);
    console.log("Get all unReadMsg!!!");
    tcpClientModule.sendPacket(ttpbHeader, unread_msg_cntReq);
  }, 300);

  // 组群页面需要加载完最近通话列表页面后加载
  setTimeout(function () {
    groupAction.getAllGroupInfo();
  }, 500);

  // 获取离线文件；

  // userInfAction.getUserInfo([1,2], function(userInfoList){
  //   console.log(userInfoList);
  // });
}

function getAllDeptList() {
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST,
    BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST);
  let dept_list = new DepartmentReq();
  dept_list.setUserId(global.myUserId);
  dept_list.setLatestUpdateTime(0);
  tcpClientModule.sendPacket(ttpbHeader, dept_list);
}

function doAllDeptListRsp(imPdu) {
  let deptListRsp = DepartmentRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let deptList = deptListRsp.getDeptListList();
  let deptNameList = {}
  for (let item of deptList) {
    deptNameList[item.getDeptId()] = item.getDeptName();
  }
  global.mainWindow.send('dept-reply', deptNameList);
}

function buddyListRoute(imPdu) {
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY:
      UserInfoAction.doUserStatNotify(imPdu);
      break;
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE:
      doBuddyListRsp(imPdu);
      break;
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_RESPONSE:
      doAllDeptListRsp(imPdu);
      break;
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_USER_INFO_RESPONSE:
      UserInfoAction.doChangeUserInfo(imPdu);
      break;
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE:
      UserInfoAction.doUserInfoRsp(imPdu);
      break;
    case BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE:
      UserInfoAction.doUserStatRsp(imPdu);
      break;
    default:
      console.log("BaseDefine_pb.BuddyListCmdID, 命令ＩＤ没有处理");
  }
}

exports.getSessJson = getSessJson;
exports.setLatestMsgId = setLatestMsgId;
exports.getRecentSession = getRecentSession;
exports.getAllDeptList = getAllDeptList;
exports.buddyListRoute = buddyListRoute;
