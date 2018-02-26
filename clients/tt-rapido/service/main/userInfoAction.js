const BrowserWindow = require('electron').BrowserWindow
var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");
var Buddy_pb = require("../pb/IM.Buddy_pb.js");
var UsersInfoReq = Buddy_pb["IMUsersInfoReq"];
var UsersStatReq = Buddy_pb["IMUsersStatReq"];
var UsersInfoRsp = Buddy_pb["IMUsersInfoRsp"];
var UsersStatRsp = Buddy_pb["IMUsersStatRsp"];
var UserStatNotify = Buddy_pb['IMUserStatNotify'];

var buddyAction = require('./buddyAction.js');
var otherAction = require('./otherAction.js');
var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var postPicture = require("../assistant/postPicture.js");
var ChangeUserInfoReq = Buddy_pb["IMChangeUserInfoReq"];
var IMChangeUserInfoRsp = Buddy_pb["IMChangeUserInfoRsp"];
var config = require('../assistant/config.js');

var userInfoMap = new Map();
var userStatMap = new Map();
var seqNumMap = new Map();

function setUserInfo(userId, userInfo){
  userInfoMap.set(userId, userInfo);
}

function getUserInfo(userId){
  return userInfoMap.get(userId);
}

function setUserStat(userId, userStat){
  userStatMap.set(userId, userStat);
}

function getUserStat(userId){
  return userStatMap.get(userId);
}

function addSeqNumMap(seqNum,userIdList, getargList, shipMap, callback){
  var seqNumObj = {};
  seqNumObj.num = seqNum;
  seqNumObj.userIdList = userIdList;
  seqNumObj.calllback = callback;
  seqNumObj.getargList = getargList;
  seqNumObj.shipMap = shipMap;
  if(callback){
    seqNumMap.set(seqNum, seqNumObj);
  }
}

function loadUserInfo(userIdList, callback){
  let unInfoList = []
  if(!userIdList){
    return;
  }
  for(let item of userIdList){
    if(!userInfoMap.has(item)){
      unInfoList.push(item);
    }
  }

  if (unInfoList.length > 0){
    let user_info_req = new UsersInfoReq();
    user_info_req.setUserId(global.myUserId);
    user_info_req.setUserIdListList(unInfoList);

    let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST, BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST);
    tcpClientModule.sendPacket(ttpbHeader, user_info_req);
    addSeqNumMap(ttpbHeader.getSeqNum(), userIdList, getUserInfoList,userInfoMap, callback);
  } else{
    callback(getUserInfoList(userIdList,userInfoMap));
  }
}

function loadUserStat(userIdList, callback){
  let unStatList = []
  for(let item of userIdList){
    if(!userStatMap.has(item)){
      unStatList.push(item);
    }
  }

  if (unStatList.length > 0){
    let user_stat_req = new UsersStatReq();
    user_stat_req.setUserId(global.myUserId);
    user_stat_req.setUserIdListList(unStatList);

    let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST,
       BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST);
    tcpClientModule.sendPacket(ttpbHeader, user_stat_req);
    addSeqNumMap(ttpbHeader.getSeqNum(), userIdList, getUserInfoList,userStatMap, callback);
  } else{
    callback(getUserInfoList(userIdList,userStatMap));
  }
}


function doSeqNumCallBack(SeqNum){
  let seqNumObj = seqNumMap.get(SeqNum);
  if(seqNumMap.get(SeqNum)){
    seqNumObj.calllback(seqNumObj.getargList(seqNumObj.userIdList, seqNumObj.shipMap));
    seqNumMap.delete(SeqNum);
  }
}

var getUserInfoList = function(userIdList, shipMap){
  let userInfoList = [];
  for(let item of userIdList){
    if(shipMap.has(item)){
      userInfoList.push(shipMap.get(item));
    }else{
      // TODO do something
      console.log("something wrong userInfo !!!",userIdList);
    }
  }
  return userInfoList;
}

//加载个人详细信息窗口
var  userSelfInfo = new Map();
function openUserInfo(userId){
  let sessLocalId = userId;
  let creatingSession = false;
  let openWindow = userSelfInfo.get(sessLocalId);
  if(openWindow){
    openWindow.show();
  }else if (creatingSession){
    console.log("正在打开对话页面");
  }else{
    creatingSession = true;
    let sessionWinJson = {
      width: config.infoWinWidth,
      height: config.infoWinHeight,
      title:'个人信息',
      frame:false,
      maximizable:false,
      transparent:true
    };
    let myInfoWindow = new BrowserWindow(sessionWinJson);
    myInfoWindow.loadURL('file://' +  global.maindir + '/main/user_info.html');

    // Debug mode
    if (config.isDev){
      myInfoWindow.webContents.openDevTools();
    }

    myInfoWindow.on('closed', function () {
      console.log("myInfoWindow: closed!!!");
      userSelfInfo.delete(sessLocalId);
    })
    myInfoWindow.webContents.on('did-finish-load', function(){
      console.log("myInfoWindow success!!!");
      userSelfInfo.set(sessLocalId, myInfoWindow);
      myInfoWindow.send('user-info-load', getUserInfo(userId));
    })
    myInfoWindow.setResizable(false); //设置窗口是否可以被用户改变size.
    creatingSession = false;
  }
}

//关闭个人信息窗口
function closeWindow(userId){
  let sessLocalId = userId;
  if (userSelfInfo.get(sessLocalId)){
    userSelfInfo.get(sessLocalId).close();
    userSelfInfo.delete(sessLocalId);
  }
}

//修改个人信息(向服务器发送请求)
function changeUserInfo(userId, tel, email, signInfo, avatar){
  let change_user_req = new ChangeUserInfoReq();
  //修改个人资料
  change_user_req.setUserId(userId);
  change_user_req.setTelephoneNum(tel);
  change_user_req.setEmailAddress(email);
  change_user_req.setSignInfo(signInfo);
  if(avatar != ''){
    //上传头像
    let imgUrl = '';
    postPicture.postPicture(avatar,function(data){
      console.log('upload: ' + data)
      imgUrl = JSON.parse(data).url;
      change_user_req.setAvatarUrl(imgUrl);
      let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST,
        BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_USER_INFO_REQUEST);
      tcpClientModule.sendPacket(ttpbHeader, change_user_req);
    });
  } else {
    let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_BUDDY_LIST,
      BaseDefine_pb.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_USER_INFO_REQUEST);
    tcpClientModule.sendPacket(ttpbHeader, change_user_req);
  }
}

//修改个人信息(服务器响应)
function doChangeUserInfo(imPdu){
  let imChangeUserInfoRsp = IMChangeUserInfoRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  if ( BaseDefine_pb.ResultType.REFUSE_REASON_NONE == imChangeUserInfoRsp.getResultCode()){
    let myInfoJosn = {
      signInfo:imChangeUserInfoRsp.getSignInfo(),
      avatar:imChangeUserInfoRsp.getAvatarUrl(),
    }

    let myinfoJson = userInfoMap.get(global.myUserId);
    myinfoJson.avatar = myInfoJosn.avatar;
    myinfoJson.signInfo = myInfoJosn.signInfo;
    global.mainWindow.send('update-myInfo', myInfoJosn);
    closeWindow(imChangeUserInfoRsp.getUserId());
  }else{
    //TODO 修改失败
  }
}

function getUserInf_Online(userSessionList, browserWindowObj = BrowserWindow.getFocusedWindow()){
  loadUserInfo(userSessionList, function(userInfoList){
    browserWindowObj.send('userinfo-reply', userInfoList);
    loadUserStat(userSessionList, function(userStatList){
      for (let item of userStatList){
        if (item.status == BaseDefine_pb.UserStatType.USER_STATUS_ONLINE){
          browserWindowObj.send('userOnline-notifi', item.userId);
        }
      }
    });
  });
}

function doUserInfoRsp(imPdu){
  console.log("load userInfo!!!");
  let userRsp = UsersInfoRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let userRspList = userRsp.getUserInfoListList();

  for (let item of userRspList){
    let itemobj ={
      "userId":item.getUserId(),
      "nickName":item.getUserNickName(),
      "signInfo":item.getSignInfo(),
      "avatar":item.getAvatarUrl(),
      "departmentId": item.getDepartmentId()
    }
    setUserInfo(item.getUserId(),itemobj);
  }
  doSeqNumCallBack(imPdu.getSeqNum());
}

function doUserStatRsp(imPdu){
  console.log("load userStat!!!");
  let user_stat_Rsp = UsersStatRsp.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let userStatList = user_stat_Rsp.getUserStatListList();
  for (let item of userStatList){
    let itemobj ={
      "userId":item.getUserId(),
      "status":item.getStatus(),
    }
    setUserStat(item.getUserId(),itemobj);
  }
  doSeqNumCallBack(imPdu.getSeqNum());
}

function doUserStatNotify(imPdu){
  let user_stat_Notify = UserStatNotify.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  let userStat = user_stat_Notify.getUserStat();
  if(getUserInfo(userStat.getUserId())){
    if(userStat.getStatus()==BaseDefine_pb.UserStatType.USER_STATUS_ONLINE){
      global.mainWindow.send('userOnline-notifi', userStat.getUserId());
    } else if(userStat.getStatus()==BaseDefine_pb.UserStatType.USER_STATUS_OFFLINE){
      global.mainWindow.send('userOffline-notifi', userStat.getUserId());
    } else {
      console.log("Can't find UserStatgetStatus userStat.getStatus():", userStat.getStatus());
    }
  } else {
    console.log("Can't find use in userInfoMap userid:", userStat.getUserId());
  }
}
function getDeptUserList(deptId) {
  // FIXME userInfoMap 可能需要从服务器端查询所有用户或部门所属所有用户
  let userList = {}
  for(let user of userInfoMap.values()) {
    if (user.departmentId == deptId) {
      userList[user.userId] = (user);
    }
  }
  global.mainWindow.send('dept-mem-reply', userList);
}
exports.getUserInfo = getUserInfo;
exports.setUserInfo = setUserInfo;
// exports.getUserStat = getUserStat;
// exports.setUserStat = setUserStat;
exports.loadUserInfo = loadUserInfo;
exports.loadUserStat = loadUserStat;
exports.getUserInf_Online = getUserInf_Online;

exports.doUserInfoRsp = doUserInfoRsp;
exports.doUserStatRsp = doUserStatRsp;
exports.doUserStatNotify = doUserStatNotify;

exports.getDeptUserList = getDeptUserList;

//exports.doSeqNumCallBack = doSeqNumCallBack;
exports.openUserInfo = openUserInfo;
exports.changeUserInfo = changeUserInfo;
exports.closeWindow = closeWindow;
exports.doChangeUserInfo = doChangeUserInfo;
