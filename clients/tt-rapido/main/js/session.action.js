function sendPicture(obj){
  if(obj.files){
    let fileObj = obj.files[0];
    let picJson = {
      picName :fileObj.name,
      picPath :fileObj.path,
      toSessionId : sessionId,
      toSessionType : sessionType,
    }
    ipcRenderer.send('send-pic', picJson);
    // 清除选择的文件
    obj.outerHTML=obj.outerHTML;
  }
}

function screenShot(){
  ipcRenderer.send('screen-shot', sessionId, sessionType);
}

function sendMsgClick(){
  let msgJson = {
    "toSessionId" : sessionId,
    "MsgType" : sessionType== SESSION_TYPE_USER ? MSG_TYPE_USER_TEXT : MSG_TYPE_GROUP_TEXT,
  }

  let content = $.trim($("#messageContent").val());
  if (content.length > 0){
    msgJson.content=content;
    ipcRenderer.send('send-msg', msgJson);
    $("#messageContent").val('');
  }
}

function accecptFile(task_id){
  //隐藏保存按钮
  $('#files_' + task_id).hide();
  $('#folder_' + task_id).show();

  ipcRenderer.send('getfile-accepct', task_id);
  $("#"+ task_id +"_perDiv").show();
}

//打开文件存放的目录
function openFolder(){
  const os = require('os');
  var filesPath = os.homedir()+"/Documents/Files";
  var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
  if(isWin){
    filesPath = filesPath. replace(/\//g, '\\');
  }
  let exec = require('child_process').exec;
  let last = exec('explorer.exe ' + filesPath, function(data){ 
    console.log(data);
   });
}

function refuseFile(task_id){
  alert("目前没做这个功能！！！");
}

//点击聊天列表中的他人的头像或者名字弹出该用户的聊天窗口
function userWindow(userId){
  ipcRenderer.send('open-session', userId, SESSION_TYPE_USER);
}

//用户组操作
function makeGroup(){
  let userObject = {};
  for (var [key, value]  of userInfoMap) {
       userObject[key] = value;
  }
  ipcRenderer.send('make-group',sessionId,sessionType,JSON.stringify(myInfo),JSON.stringify(userObject));
}
