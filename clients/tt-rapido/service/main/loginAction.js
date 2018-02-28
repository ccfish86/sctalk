const {app, BrowserWindow} = require('electron');

const os = require('os');
const fs = require('fs');

var constant = require('../assistant/constant.js');
var config = require('../assistant/config.js');
var BaseDefine_pb = require('../pb/IM.BaseDefine_pb.js');
var Login_pb = require('../pb/IM.Login_pb.js');
var LoginReq = Login_pb['IMLoginReq'];
var IMLoginRes = Login_pb['IMLoginRes'];

var helper = require("../assistant/helper.js");
var ttPBHeader = require('../assistant/TTPBHeader');
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var buddyAction = require('./buddyAction.js');
var otherAction = require('./otherAction.js');
var UserInfoAction = require("./userInfoAction.js");


function checkAutoLogin(){
  fs.exists(global.datadir+'/'+ constant.LOGIN_CONF, (exists) => {
    if(exists){
      fs.readFile(global.datadir+'/'+ constant.LOGIN_CONF, (err,data) => {
        if(!err){
          let configJson = JSON.parse(data);
          let loginfoObj = configJson.loginparameter;
          if(loginfoObj.autologin){
            dologin(loginfoObj.username, loginfoObj.password);
          } else if(loginfoObj.savePassword){
            global.mainWindow.send('load-userInfo', loginfoObj.username, loginfoObj.password);
          }
        }
      });
    } else {
      // TODO 无法创建用户数据文件夹，Do something！！！
    }
  });
}

function dologin(username, password, autologin, savePassword){
  let login_req = new LoginReq();
  login_req.setUserName(username);
  login_req.setPassword(password);
  login_req.setClientVersion(constant.CLIENT_VERSION);
  login_req.setOnlineStatus(BaseDefine_pb.UserStatType.USER_STATUS_ONLINE);

  login_req.setClientType(BaseDefine_pb.ClientType.CLIENT_TYPE_WINDOWS);

  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_LOGIN,
          BaseDefine_pb.LoginCmdID.CID_LOGIN_REQ_USERLOGIN);
  tcpClientModule.proiorInit();
  tcpClientModule.sendPacket(ttpbHeader, login_req);

  if(autologin || savePassword){
    let serialize = login_req.serializeBinary();
    fs.exists(global.datadir, (exists) => {
      if(!exists){
        console.log("Create login dir!!!");
        fs.mkdirSync(global.datadir)
      }
      loginParameter = {
        username : username,
        password : password,
        clientVersion : constant.CLIENT_VERSION,
        clientType : BaseDefine_pb.ClientType.CLIENT_TYPE_WINDOWS,
        autologin: autologin,
        savePassword: savePassword,
      }

      loginConfig = {};
      loginConfig.loginparameter = loginParameter;
      //console.log("config:", loginConfig);
      fs.writeFile(global.datadir+'/'+ constant.LOGIN_CONF,
        JSON.stringify(loginConfig) , (err) => {
          if(err){
              // TODO 保存登陆信息失败
          }
      });
    });
  }
};

function doLoginRsp(imPdu){
  let imLoginRes = IMLoginRes.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
  console.log("server_time:", imLoginRes.getServerTime());
  console.log("result_code:", imLoginRes.getResultCode(), BaseDefine_pb.ResultType.REFUSE_REASON_NONE);
  console.log("result_string:", imLoginRes.getResultString());
  console.log("online_status:", imLoginRes.getOnlineStatus());

  if ( BaseDefine_pb.ResultType.REFUSE_REASON_NONE == imLoginRes.getResultCode()){
    let myInfo = imLoginRes.getUserInfo();
    global.myUserId = myInfo.getUserId();
    let myobj ={
      "userId":myInfo.getUserId(),  //用户id
      "nickName":myInfo.getUserNickName(),  //用户昵称
      "avatar":myInfo.getAvatarUrl(), //用户头像url
      "signInfo":myInfo.getSignInfo(), //用户签名信息
      "gender":myInfo.getUserGender(),  //用户性别
      "department":myInfo.getDepartmentId(),  //用户所属部门
      "email":myInfo.getEmail(),  //用户邮箱
      "realName":myInfo.getUserRealName(),  //用户登录名
      "tel":myInfo.getUserTel(),  //用户电话
    }
    UserInfoAction.setUserInfo(myInfo.getUserId(),myobj);
    otherAction.startHeartBeat();
    global.mainWindow.loadURL('file://' + global.maindir + '/main/main.html')
    global.mainWindow.webContents.on('did-finish-load', function(){
      // 获取所有人信息
      buddyAction.getAllUser();
      // 获取部门信息
      buddyAction.getAllDeptList();
      buddyAction.getRecentSession();
    });
  } else{
    //密码错误处理(ruilu)
    global.mainWindow.send('login-error', imLoginRes.getResultString());
  }
}

function relogin(){
    for (let item of BrowserWindow.getAllWindows()){
      item.close();
    }
    app.relaunch()
    app.exit(0)
}

function loginRoute (imPdu){
  var cmdId = imPdu.getCmdid();
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.LoginCmdID.CID_LOGIN_RES_USERLOGIN:
    doLoginRsp(imPdu);
    break;
  default:
    console.log("BaseDefine_pb.LoginCmdID, 命令ＩＤ没有处理");
  }
};

exports.checkAutoLogin = checkAutoLogin;
exports.dologin = dologin;
exports.loginRoute = loginRoute;
exports.relogin = relogin