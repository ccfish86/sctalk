const BrowserWindow = require('electron').BrowserWindow
var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");
var Buddy_pb = require("../pb/IM.Buddy_pb.js");
var buddyAction = require('./buddyAction.js');
var helper = require("../assistant/helper.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var config = require('../assistant/config.js');

var  settingWin = new Map();
function openSetting(userId){
  let sessLocalId = userId;
  let creatingSession = false;
  let openWindow = settingWin.get(sessLocalId);
  if(openWindow){
    openWindow.show();
  }else if (creatingSession){
    console.log("正在打开对话页面");
  }else{
    creatingSession = true;
    let sessionWinJson = {
      width: config.settingWinWidth,
      height: config.settingWinHeight,
      title:'系统设置',
      frame:false,
      maximizable:false,
      transparent:true
    };
    let settingWindow = new BrowserWindow(sessionWinJson);
    settingWindow.loadURL('file://' +  global.maindir + '/main/setting.html');

    //开启调试模式
    if (config.isDev){
      settingWindow.webContents.openDevTools();
    }

    settingWindow.on('closed', function () {
      console.log("settingWindow: closed!!!");
      settingWin.delete(sessLocalId);
    })
    settingWindow.webContents.on('did-finish-load', function(){
      console.log("settingWindow success!!!");
      settingWin.set(sessLocalId, settingWindow);
      settingWindow.send('setting-load', userId);
    })
    settingWindow.setResizable(false); //设置窗口是否可以被用户改变size.
    creatingSession = false;
  }
}

//关闭个人信息窗口
function closeWindow(userId){
  let sessLocalId = userId;
  if (settingWin.get(sessLocalId)){
    settingWin.get(sessLocalId).close();
    settingWin.delete(sessLocalId);
  }
}

exports.openSetting = openSetting;
exports.closeWindow = closeWindow;