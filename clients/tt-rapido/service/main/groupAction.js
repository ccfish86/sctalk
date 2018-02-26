var BaseDefine = require("../pb/IM.BaseDefine_pb.js");
var GroupEditAction = require("./groupEditAction.js");
var Group_pb = require("../pb/IM.Group_pb.js");
var normalGroupListReq = Group_pb["IMNormalGroupListReq"];
var normalGroupListRsp = Group_pb["IMNormalGroupListRsp"];
var groupInfoListReq = Group_pb["IMGroupInfoListReq"];
var groupInfoListRsp = Group_pb["IMGroupInfoListRsp"];

var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');

var groupMap = new Map();

function getGroupInfo(groupId){
  return groupMap.get(groupId);
}

function setGroupInfo(groupId, gInfoJson){
  groupMap.set(groupId, gInfoJson);
}

function getAllGroupInfo(){
  console.log("getAllGroupInfo!!!");
  let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,
      BaseDefine.GroupCmdID.CID_GROUP_NORMAL_LIST_REQUEST);
  let groupListReq = new normalGroupListReq();
  groupListReq.setUserId(global.myUserId);
  tcpClientModule.sendPacket(ttpbHeader, groupListReq);
}

function doGroupListRsp(imPdu){
  console.log("doGroupListRsp!!!");
  let groupListRsp = normalGroupListRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let userGroupList = groupListRsp.getGroupVersionListList();

  let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,
      BaseDefine.GroupCmdID.CID_GROUP_INFO_REQUEST);
  let gInfoListReq = new groupInfoListReq();
  gInfoListReq.setUserId(global.myUserId);
  for (let item of userGroupList){
    item.setVersion(0);
  }
  gInfoListReq.setGroupVersionListList(userGroupList);
  tcpClientModule.sendPacket(ttpbHeader, gInfoListReq);
}

function doGroupInfoListRsp(imPdu){
  console.log("doGroupInfoListRsp!!!");
  let gInfListRsp = groupInfoListRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let groupInfoList = []
  for (let item of gInfListRsp.getGroupInfoListList()){
    let gInfoJson = item.toObject();
    gInfoJson.nickName = gInfoJson.groupName
    groupInfoList.push(gInfoJson);
    groupMap.set(item.getGroupId(), gInfoJson);
  }
  global.mainWindow.send('groupinfo-reply', groupInfoList);
}

function groupRoute(imPdu){
  switch (imPdu.getCmdid()) {
    case BaseDefine.GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE:
      doGroupListRsp(imPdu);
      break;
    case BaseDefine.GroupCmdID.CID_GROUP_INFO_RESPONSE:
      doGroupInfoListRsp(imPdu);
      break;
    case BaseDefine.GroupCmdID.CID_GROUP_CREATE_RESPONSE:
      GroupEditAction.doGroupCreateRsp(imPdu);
      break;
    case BaseDefine.GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE:
      GroupEditAction.doGroupChangeMemberResponse(imPdu);
      break;
    case BaseDefine.GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY:
      console.log("_________________________");
      break;
    case BaseDefine.GroupCmdID.CID_GROUP_CREATE_NOTIFY:
      GroupEditAction.doGroupCreateNtf(imPdu);
      break;
    default:
      console.log("BaseDefine_pb.GroupCmdID, Can't get CMDId:", imPdu.getCmdid());
  }
}

exports.getGroupInfo = getGroupInfo;
exports.setGroupInfo = setGroupInfo;
exports.getAllGroupInfo = getAllGroupInfo;
exports.groupRoute = groupRoute;
