var loginAction = require('./loginAction.js');
var buddyAction = require('./buddyAction.js');
var groupAction = require('./groupAction.js');
var groupEditAction = require('./groupEditAction.js');
var messgAction = require('./messgAction.js');
var fileAction = require('./fileAction.js');
var sessionAction = require('./sessionAction.js');
var sendPicAction = require("./sendPicAction.js");
var UserInfoAction = require("./userInfoAction.js");
var SettingAction = require("./settingAction.js");
var trayAction = require("./trayAction.js");
var sendP2pAction = require("./sendP2pAction.js");

exports.ipcInit = function() {
  var ipc = require('electron').ipcMain;

  // main window
  ipc.on('send-data', function(event, m, m2) {
    global.priorConn.senddata(m);
  });

  ipc.on('login-req', function(event, username, passwd, autologin, savePassword) {
    loginAction.dologin(username, passwd, autologin, savePassword);
  });

  ipc.on('get-myinfo', (event, arg) => {
    // myInfoObj = UserInfoAction.getUserInfo(global.myUserId);
    let myInfoAsynResult = UserInfoAction.getUserInfo(global.myUserId);
    myInfoAsynResult.then(infoObj => {
      console.info('myInfoAsynResult ' + global.myUserId)
      // event.returnValue = JSON.stringify(myInfoObj);
      global.mainWindow.send('get-myinfo-rsp', infoObj)
    }).catch(err=>{
      // 加载个人信息异常
      // event.returnValue = {}
      global.mainWindow.send('get-myinfo-rsp', {})
    })
  })

  ipc.on('load-userOnlineStat', (event, userIdList) => {
    UserInfoAction.getUserInf_Online(userIdList);
  })

  ipc.on('get-groupInfo', (event, arg) => {
    groupAction.getAllGroupInfo();
  })

  ipc.on('open-session', (event, sessionId, sessionType) => {
    let sessionWindon = sessionAction.openSession(sessionId, sessionType);
    event.returnValue = sessionWindon;
  })

  ipc.on('setting-Win', (event) => {
    SettingAction.openSetting(global.myUserId);
  })

  ipc.on('mainWin-mini', (event) => {
    global.mainWindow.blur();
    global.mainWindow.hide();
  })

  ipc.on('client-online', (event) => {
    loginAction.relogin();
  })

  ipc.on('client-offline', (event) => {
    trayAction.offLineTray();
  })

  // session window
  ipc.on('get-historyMsg', (event, sessionId, sessionType, latestMsgId) => {
    sessionAction.getHistoryMsg(sessionId, sessionType, latestMsgId);
  })

  ipc.on('send-file', (event, fileObj) => {
    fileAction.sendFileReq(fileObj);
  })

  ipc.on('getfile-accepct', (event, taskId) => {
    fileAction.accecptFile(taskId);
  })

  ipc.on('send-pic', (event, picObj, sessionId) => {
    sendPicAction.sendPicReq(picObj);
  })

  ipc.on('screen-shot', (event, sessionId, sessionType) => {
    sendPicAction.screenShot(sessionId, sessionType);
  })
  ipc.on('screen-shake', (event, sessionId, sessionType) => {
    sendP2pAction.screenShake(sessionId, sessionType);
  })

  ipc.on('sessionWin-close', (event, sessionId, sessionType) => {
    sessionAction.closeWindow(sessionId, sessionType);
  })

  ipc.on('sessionWin-max', (event, sessionId, sessionType) => {
    sessionAction.maxWindow(sessionId, sessionType);
  })

  ipc.on('sessionWin-mini', (event, sessionId, sessionType) => {
    sessionAction.miniWindow(sessionId, sessionType);
  })

  ipc.on('send-msg', (event, arg) => {
    messgAction.sendMsg(arg);
  })

  ipc.on('make-group', (event,sessionId, sessionType, userInfo) => {
    //直接创建讨论组
    if(sessionId == undefined && sessionType == undefined && userInfo == undefined){
      let SESSION_TYPE_USER = 1;
      let userInfo = JSON.stringify(global.myInfoObj);
      groupEditAction.openGroupUpate(global.myUserId, SESSION_TYPE_USER, userInfo);
    }else{
      //个人聊天窗口创建讨论组
      groupEditAction.openGroupUpate(sessionId, sessionType, userInfo);
    }
  })


  // group edit window
  ipc.on('sessionGroupWin-close', (event, sessionId, sessionType) => {
    groupEditAction.closeWindow(sessionId, sessionType);
  })

  ipc.on('session-group-update', (event, sessionId, sessionType,updateTempGroupInfo) => {
    groupEditAction.sessionGroupUpdate(sessionId, sessionType,updateTempGroupInfo);
  })

  ipc.on('settingWin-close', (event) => {
    SettingAction.closeWindow(global.myUserId);
  })


  // My info window
  //加载自己详细信息
  ipc.on('user-info', (event) => {
     UserInfoAction.openUserInfo(global.myUserId);
  })
  //修改自己资料(电话、邮箱和签名)
  ipc.on('user-edit', (event, userId, tel, email, signInfo, avatar) => {
    UserInfoAction.changeUserInfo(userId, tel, email, signInfo, avatar);
  })
  //关闭个人资料窗口
  ipc.on('sessionMyWin-close', (event, userId) => {
    UserInfoAction.closeWindow(userId);
  })

  ipc.on('dept-mem', (event, deptId) => {
    UserInfoAction.getDeptUserList(deptId)
  })
}
