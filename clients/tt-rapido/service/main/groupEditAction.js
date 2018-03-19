const BrowserWindow = require('electron').BrowserWindow
var UserInfoAction = require("./userInfoAction.js");
var SessionAction = require("./sessionAction.js");
var groupAction = require("./groupAction.js");

var BaseDefine = require("../pb/IM.BaseDefine_pb.js");
var Group_pb = require("../pb/IM.Group_pb.js");
var groupCreateReq = Group_pb["IMGroupCreateReq"];
var groupCreateRsp = Group_pb["IMGroupCreateRsp"];
var groupChangeMemberReq = Group_pb["IMGroupChangeMemberReq"];
var groupChangeMemberRsp = Group_pb["IMGroupChangeMemberRsp"];
var groupCreateNotify = Group_pb["IMGroupCreateNotify"];

var config = require("../assistant/config.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var PostPicture = require("../assistant/postPicture.js");
var helper = require("../assistant/helper.js");

var windowGroupType = new Map();
var groupUserCheck = new Map();
var sendGroupMsg = new Map();

//用户组
function openGroupUpate(sessionId, sessionType, userInfo){
  let sessLocalId = sessionId + "_" + sessionType;
  let creatingSession = false;
  let openWindow = windowGroupType.get(sessLocalId);
  if(openWindow){
    openWindow.show();
  }else if (creatingSession){
    console.log("正在打开对话页面");
  }else{
    creatingSession = true;
    let sessionWinJson = {
      width: config.groupWinWidth,
      height: config.groupWinHeight,
      title:'创建讨论组',
      frame:false,
      maximizable:true,
      transparent:true
    };
    let sessionWindow = new BrowserWindow(sessionWinJson);
    sessionWindow.loadURL('file://' +  global.maindir + '/main/make_group.html');

    // Debug mode
    if (config.isDev){
      sessionWindow.webContents.openDevTools();
    }

    sessionWindow.on('closed', function () {
      console.log("sessionWindow: closed!!!");
      windowGroupType.delete(sessLocalId);
    })
    let tempGroupInfo = {};
    if (sessionType == BaseDefine.SessionType.SESSION_TYPE_GROUP){
      tempGroupInfo= groupAction.getGroupInfo(sessionId);
      UserInfoAction.loadUserInfo(tempGroupInfo.groupMemberListList, function(userInfoList){
        tempGroupInfo.showGroupList=userInfoList;
      });
    }
    if (sessionType == BaseDefine.SessionType.SESSION_TYPE_SINGLE){
       tempGroupInfo.showGroupList = [UserInfoAction.getUserInfo(sessionId)];
    }
    sessionWindow.webContents.on('did-finish-load', function(){
      windowGroupType.set(sessLocalId, sessionWindow);
      sessionWindow.send('group-session-load', sessionId,sessionType, tempGroupInfo,userInfo);
    })
    sessionWindow.setResizable(false); //设置窗口是否可以被用户改变size.
    creatingSession = false;
  }
}

function closeWindow(sessionId, sessionType){
  console.log(sessionId + "_" + sessionType);
  let sessLocalId = sessionId + "_" + sessionType;
  if (windowGroupType.get(sessLocalId)){
    console.log(sessLocalId+" group update closeWindow  console");
    windowGroupType.get(sessLocalId).close();
    windowGroupType.delete(sessLocalId);
  }
}

function ceckSendMsgStat(SeqNum){
  console.log('SeqNum');
  console.log(SeqNum);
}

//update group info
function sessionGroupUpdate(sessionId, sessionType,updateTempGroupInfo) {
  if(updateTempGroupInfo.group_id){
    if(updateTempGroupInfo.remove_id_list != ""){
      //groupUserCheck.set("remove",1);
      let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,BaseDefine.GroupCmdID. CID_GROUP_CHANGE_MEMBER_REQUEST);
      let groupCreateReqs = new groupChangeMemberReq();
      //先执行删除吧
      groupCreateReqs.setChangeType(BaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_DEL);
      groupCreateReqs.setGroupId(updateTempGroupInfo.group_id);
      groupCreateReqs.setMemberIdListList(updateTempGroupInfo.remove_id_list);
      groupCreateReqs.setUserId(updateTempGroupInfo.user_id);
      tcpClientModule.sendPacket(ttpbHeader, groupCreateReqs);
      console.log(ttpbHeader.getSeqNum());
      if(updateTempGroupInfo.member_id_list != ""){
        sendGroupMsg.set(ttpbHeader.getSeqNum(), 1);
      }else{
        sendGroupMsg.set(ttpbHeader.getSeqNum(), sessionId + "_" + sessionType);
      }
    }

    if(updateTempGroupInfo.member_id_list != ""){
       let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,
       BaseDefine.GroupCmdID. CID_GROUP_CHANGE_MEMBER_REQUEST);
       let groupChangeMemberReqs = new groupChangeMemberReq();
       groupChangeMemberReqs.setChangeType(BaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_ADD);
       groupChangeMemberReqs.setGroupId(updateTempGroupInfo.group_id);
       groupChangeMemberReqs.setMemberIdListList(updateTempGroupInfo.member_id_list);
       groupChangeMemberReqs.setUserId(updateTempGroupInfo.user_id);
       tcpClientModule.sendPacket(ttpbHeader, groupChangeMemberReqs);
       sendGroupMsg.set(ttpbHeader.getSeqNum(), sessionId + "_" + sessionType);
    }
  }else{
    if(updateTempGroupInfo.group_avatar != ''){
         PostPicture.postPicture(updateTempGroupInfo.group_avatar,function(data){
            updateTempGroupInfo.group_avatar = JSON.parse(data).url;
            let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,BaseDefine.GroupCmdID.CID_GROUP_CREATE_REQUEST);
            let groupCreateReqs = new groupCreateReq();
            groupCreateReqs.setGroupAvatar(updateTempGroupInfo.group_avatar);
            groupCreateReqs.setGroupType(BaseDefine.GroupType.GROUP_TYPE_TMP);
            groupCreateReqs.setGroupName(updateTempGroupInfo.group_name);
            groupCreateReqs.setMemberIdListList(updateTempGroupInfo.member_id_list);
            groupCreateReqs.setUserId(updateTempGroupInfo.user_id);
            tcpClientModule.sendPacket(ttpbHeader, groupCreateReqs);
            sendGroupMsg.set(ttpbHeader.getSeqNum(), sessionId + "_" + sessionType);
        });
    }else{
      let ttpbHeader = helper.getPBHeader(BaseDefine.ServiceID.SID_GROUP,BaseDefine.GroupCmdID.CID_GROUP_CREATE_REQUEST);
      let groupCreateReqs = new groupCreateReq();
      groupCreateReqs.setGroupAvatar(updateTempGroupInfo.group_avatar);
      groupCreateReqs.setGroupType(BaseDefine.GroupType.GROUP_TYPE_TMP);
      groupCreateReqs.setGroupName(updateTempGroupInfo.group_name);
      groupCreateReqs.setMemberIdListList(updateTempGroupInfo.member_id_list);
      groupCreateReqs.setUserId(updateTempGroupInfo.user_id);
      tcpClientModule.sendPacket(ttpbHeader, groupCreateReqs);
      sendGroupMsg.set(ttpbHeader.getSeqNum(), sessionId + "_" + sessionType);
    }
  }
}

function doGroupChangeMemberResponse(imPdu) {
   let imPduMsId = imPdu.getSeqNum();
   let groupChangeMemberList =  groupChangeMemberRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
   let groupChangeMemberObject = groupChangeMemberList.toObject();
   if(groupChangeMemberObject.resultCode == 0){
      groupAction.getGroupInfo(groupChangeMemberObject.groupId).groupMemberListList = groupChangeMemberObject.curUserIdListList;
      if(groupChangeMemberObject.changeType == BaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_DEL)
      {
        if(sendGroupMsg.get(imPduMsId) != 1)
        {
          //update groupMap
          windowGroupType.get(sendGroupMsg.get(imPduMsId)).close();
        }
      }
      if(groupChangeMemberObject.changeType == BaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_ADD)
      {
        windowGroupType.get(sendGroupMsg.get(imPduMsId)).close();
        //groupMap.get(groupChangeMemberObject.groupId).groupMemberList = groupChangeMemberObject.curUserIdListList;
      }
      sendGroupMsg.delete(imPduMsId);
   }else{
      //操作失败处理
     //todo
   }

   let sessionInfo =  groupAction.getGroupInfo(groupChangeMemberObject.groupId);
   let sessWindows = SessionAction.SessObjMap.get(sessionInfo.toSessionId+"_"+sessionInfo.toSessionType);
   UserInfoAction.loadUserInfo(groupChangeMemberObject.curUserIdListList, function(userInfoList){
        sessWindows.send('gmember-replace-load', userInfoList);
   });
}

function doGroupCreateRsp(imPdu) {
  let groupListRsp = groupCreateRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let groupListRspObject = groupListRsp.toObject();
  if(groupListRspObject.resultCode == 0){
    //susess
    groupListRspObject.nickName = groupListRspObject.groupName;
    groupListRspObject.version = 1;
    groupListRspObject.groupCreatorId = groupListRspObject.userId;
    groupListRspObject.groupType = BaseDefine.GroupType.GROUP_TYPE_TMP;
    groupListRspObject.shieldStatus = 0;
    groupListRspObject.groupMemberListList = groupListRspObject.userIdListList;
    groupListRspObject.groupAnnouncement = '';
    groupListRspObject.msgId = 0;
    groupAction.setGroupInfo(groupListRspObject.groupId, groupListRspObject);
    global.mainWindow.send('groupinfo-add', groupListRspObject);
    //关闭创建窗口
    windowGroupType.get(sendGroupMsg.get(imPdu.getSeqNum())).close();
  }else{
    console.log("create temp group faild!");
  }
  sendGroupMsg.delete(imPdu.getSeqNum());
};

function doGroupCreateNtf(imPdu){
  let gCreateNty = groupCreateNotify.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let gInfoJson = gCreateNty.toObject();
  gInfoJson.version = 1;
  gInfoJson.groupAvatar = '';
  gInfoJson.groupCreatorId = gInfoJson.userId;
  gInfoJson.groupType = BaseDefine.GroupType.GROUP_TYPE_TMP;
  gInfoJson.shieldStatus = 0;
  gInfoJson.groupMemberListList = gInfoJson.userIdListList;
  gInfoJson.nickName = gInfoJson.groupName;
  groupAction.setGroupInfo(gInfoJson.groupId, gInfoJson);
  global.mainWindow.send('groupinfo-reply', [gInfoJson]);
}

exports.openGroupUpate = openGroupUpate;
exports.doGroupChangeMemberResponse = doGroupChangeMemberResponse;

exports.windowGroupType = windowGroupType;
exports.closeWindow = closeWindow;
exports.sessionGroupUpdate = sessionGroupUpdate;
exports.doGroupCreateRsp = doGroupCreateRsp;
exports.doGroupCreateNtf = doGroupCreateNtf;
