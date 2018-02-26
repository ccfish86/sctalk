const {ipcRenderer} = require('electron');
const spawn = require('child_process').spawn;
var moment = require('moment');
var SESSION_TYPE_USER= 1;
var SESSION_TYPE_GROUP = 2;
var MSG_TYPE_USER_TEXT  = 0x1;
var MSG_TYPE_GROUP_TEXT  = 0x11;
var MSG_FROM_ME = "me";
var MSG_FROM_OTHER = "other";
var MSG_DISPLAY_TYPE_TEXT  = 1;
var MSG_DISPLAY_TYPE_IMG  = 2;
var MSG_DISPLAY_TYPE_SFILE  = 3;
var MSG_DISPLAY_TYPE_SYSTEM  = 4;
var MSG_DISPLAY_TYPE_TFILE  = 5;
var MSG_DISPLAY_TYPE_VOICE  = 6;
var sessionId, sessionType, latestMsgId, myInfo;
var userInfoMap = new Map();

function windowsImgView(imgObj) {
  let last =  spawn('C:\\Windows\\System32\\rundll32.exe', ["C:\\Program Files\\Windows\ Photo\ Viewer\\PhotoViewer.dll", 'ImageView_Fullscreen', $(imgObj).attr("src")]);
  last.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });
  last.stderr.on('data', (data) => {
    console.log(`stderr: ${data}`);
  });
  last.on('close', (code) => {
    console.log(`child process exited with code ${code}`);
  });
}

function getGroupMemberLi(userInfo){
  let userLi = $("<li></li>");
  let userA = $("<a href＝'#' style='padding: 5px'></a>");
  let userRowDiv = $("<div></div>");
  let userAvarImg= $('<img class="img-circle" style="margin-right:10px;">')
  if (userInfo.avatar){
      userAvarImg.attr('src',userInfo.avatar);
  } else {
    userAvarImg.attr('src',"css/img/main/session.png");
  }
  userRowDiv.append(userAvarImg);
  userRowDiv.append(userInfo.nickName);
  userA.append(userRowDiv);
  userLi.append(userA);
  //群组中打开选择的个人聊天窗口
  userLi.dblclick(function(){
    ipcRenderer.send('open-session', userInfo.userId, SESSION_TYPE_USER);
  });
  return userLi;
}

function sessOnload(){
  document.ondragover = document.ondrop = (ev) => {
    ev.preventDefault()
  }
  document.body.ondrop = (ev) => {
    let fileObj = ev.dataTransfer.files[0];
    fileJson = {
      fileName:fileObj.name,
      filePath:fileObj.path,
      fileSize:fileObj.size,
      toObjUserId:sessionId,
    }
    ipcRenderer.send('send-file', fileJson);
    console.log("drapFile:",ev.dataTransfer.files[0])
    ev.preventDefault()
  }

  document.onkeydown = function(event){
    let e = event;
    if (e.keyCode == 83 && e.altKey) {
      sendMsgClick();
    }
  };

  $('.emotion').qqFace({
    id : 'facebox',
    assign:'messageContent',
    path:'css/img/session/Face/',
    faceJson: faceJson,
  });
}
