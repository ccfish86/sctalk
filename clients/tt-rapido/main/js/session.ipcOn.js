$(function(){
  ipcRenderer.on('objInfo-load', (event, arg,userId) => {
    if (arg.toSessionId){
      sessionId = arg.toSessionId;
    } else {
      alert("无法获取消息ID");
      // TODO 关闭这个对话框
    }
    $("#obj_name_main").html(arg.nickName);
    //根据toSessionType类型，调用不同头像
    if(arg.toSessionType == 1){
      $("#obj_user_main").attr('title',arg.userId);
      if(arg.avatar){
        //自定义头像
        $("#obj_user_main").html('<img src="'+arg.avatar+'" class="user_info_pic" />');
      }else{
        //默认头像
        $("#obj_user_main").html('<img src="css/img/main/session.png" class="user_info_pic" />');
      }
    }else{
      $("#obj_avar_main").attr('title',(arg.groupMemberListList.length)+"人");
      if(arg.groupAvatar){
        //自定义头像
        $("#obj_avar_main").html('<img src="'+arg.groupAvatar+'" class="user_info_pic" />');
      }else{
        //默认头像
        $("#obj_avar_main").html('<img src="css/img/main/Groups.png" class="user_info_pic" />');
      }
    }

    if(arg.toSessionType == SESSION_TYPE_USER){
      $("#odj_user_info").html('<li style="font-weight:bold;line-height:23px;">'+arg.nickName+'</li><li style="line-height:22px;font-size:12px;">'+arg.signInfo+'</li>');
      userInfoMap.set(arg.toSessionId, arg);
      sessionType = SESSION_TYPE_USER;
      $("#sessMainDiv").css("right", "0px");
      if(arg.userId != userId){
          $("#toolBarDiv").append('<div title="创建组群" class="icon-toolbar-item text-center" style="right:10px;width: 25px;text-align: center;" onclick="makeGroup('+arg.userId+')"><img src="css/img/session/adduser.png" width="23px"></div>');
      }
    }else{
      sessionType = SESSION_TYPE_GROUP;
      $("#odj_user_info").html('<li style="line-height:45px;font-weight:bold;">'+arg.nickName+'</li>');
      $("#groupAddonDiv").show();
      $("#sessMainDiv").css("right", "160px");
      if(arg.toSessionType == SESSION_TYPE_GROUP && arg.groupCreatorId == userId){
        $("#toolBarDiv").append('<div title="创建组群" class="icon-toolbar-item text-center" style="right:10px;width: 25px;text-align: center;" onclick="makeGroup('+arg.userId+')"><img src="css/img/session/adduser.png" width="23px"></div>');
      }
    }
    latestMsgId = arg.msgId;
  });

  ipcRenderer.on('myInfo-load', (event, arg) => {
    if (arg){
      myInfo = arg;
    } else {
      alert("无法获取用户ID");
      // TODO 关闭这个对话框
    }
  });

  ipcRenderer.on('gmember-load', (event, arg) => {
    for (let item of arg){
      $("#gmbottomLi").before(getGroupMemberLi(item));
      userInfoMap.set(item.userId, item);
    }
  });

ipcRenderer.on('gmember-replace-load', (event, arg) => {
     $("#groupMemberUl").html('');

     $("#groupMemberUl").append($('<li id="gmbottomLi" style="list-style-type:none;"></li>'));
    for (let item of arg){
      $("#gmbottomLi").before(getGroupMemberLi(item));
      userInfoMap.set(item.userId, item);
    }
  });

  /*
  msgJson = {
    fromUserId,
    toSessionId,
    toSessionType,
    msgId,
    msgData,
    msgType,
    msgTime,
  }
  */
  ipcRenderer.on('msg-show', (event, arg) => {
    arg.reverse();
    for (let item of arg){
      let userInfo, mtype;
      if(myInfo.userId == item.fromUserId){
        userInfo= myInfo;
        mtype = MSG_FROM_ME;
      } else{
        userInfo = userInfoMap.get(item.fromUserId);
        mtype =MSG_FROM_OTHER;
      }
      console.info('userInfoMap):'+ userInfoMap)

      let showMsg = getMsgJson(userInfo.nickName, userInfo.userId, item.msgTime, userInfo.avatar, MSG_DISPLAY_TYPE_TEXT, item.msgData, mtype);
      sendMessage(JSON.stringify(showMsg));
    }
  });

  ipcRenderer.on('sessMsg-shortCut', (event) => {
    sendMsgClick();
  });

  ipcRenderer.on('msg-history', (event, arg) => {
    console.log("get-historyMsg:", arg);
    let messages = []
    for (let i=1; i<=arg.length;i++){
      let item = arg[arg.length-i];
      if(i == 1 && arg.length > 10){
        latestMsgId = item.msgId;
      } else {
        let nickname,userAvatar;
        if (myInfo.userId == item.fromUserId){
          nickName = myInfo.nickName;
          userId = myInfo.userId;
          userAvatar = myInfo.avatar;
          mtype =MSG_FROM_ME;
        }else{
          let objInfo = userInfoMap.get(item.fromUserId);
          nickName = objInfo.nickName;
          userId = objInfo.userId;
          userAvatar = objInfo.avatar;
          mtype =MSG_FROM_OTHER;
        }
        let showMsgType;
        if(MSG_TYPE_USER_TEXT ==item.msgType || MSG_TYPE_GROUP_TEXT == item.msgType){
            showMsgType = MSG_DISPLAY_TYPE_TEXT;
        } else {
          alert("错误的消息类型："+item.msgType);
          continue;
        }
        let showMsg = getMsgJson(nickName, userId, item.msgTime, userAvatar, showMsgType, item.msgData, mtype);
        messages.push(showMsg);
      }
    }
    console.log("messages:", messages);
    messages = JSON.stringify(messages);
    historyMessage(messages);
  });

  ipcRenderer.on("sendFile-req", (event, sendFileObj) =>{
    if($("#"+sendFileObj.task_id+"_perDiv").length ==0){
      var fileMsg = {
        msgtype: MSG_DISPLAY_TYPE_TFILE,
        filepath: sendFileObj.filePath,
        filename: sendFileObj.fileName,
        filesize: bytesToSize(sendFileObj.fileSize),
        fileid: sendFileObj.task_id,
        task_id:sendFileObj.task_id,
        sendtime:sendFileObj.filetime,
      };
      sendMessage(JSON.stringify(fileMsg));
    }
    $("#"+sendFileObj.task_id+"_perDiv").find(".progress-in")
      .css("width", sendFileObj.percent+"%");
    $("#"+sendFileObj.task_id+"_perDiv").find(".percent-show")
      .html(sendFileObj.percent+"%");
  })

  ipcRenderer.on("getFile-req", (event, getFileObj) =>{
    $("#"+getFileObj.task_id+"_perDiv").find(".progress-in")
      .css("width", getFileObj.percent+"%");
    $("#"+getFileObj.task_id+"_perDiv").find(".percent-show")
      .html(getFileObj.percent+"%");
  })

  ipcRenderer.on("resvFile-notif", (event, resvFileObj) =>{
    if($("#"+resvFileObj.task_id+"_perDiv").length ==0){
      var fileMsg = {
        msgtype: MSG_DISPLAY_TYPE_SFILE,
        filename: resvFileObj.fileName,
        filesize: bytesToSize(resvFileObj.fileSize),
        fileid: resvFileObj.task_id,
        task_id:resvFileObj.task_id,
        sendtime:resvFileObj.filetime,
      };
      sendMessage(JSON.stringify(fileMsg));
    }
  });

  ipcRenderer.on("sendFile-result", (event, sendFileObj) =>{
    console.log("sendFile-result:", sendFileObj);
    var fileSysMsg = {
      msgtype: MSG_DISPLAY_TYPE_SYSTEM,
      content: sendFileObj.content,
    };
    sendMessage(JSON.stringify(fileSysMsg));
    if($("#"+sendFileObj.task_id+"_perDiv")){
      $("#"+sendFileObj.task_id+"_perDiv").find("a").hide();
    }
  });
})

function closeWindow(){
  ipcRenderer.send('sessionWin-close', sessionId, sessionType);
}

function maxWindow(){
  ipcRenderer.send('sessionWin-max', sessionId, sessionType);
}

function miniWindow(){
  ipcRenderer.send('sessionWin-mini', sessionId, sessionType);
}

function bytesToSize(bytes) {
  if (bytes === 0) return '0 B';
  var k = 1024;
  sizes = ['B','KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  i = Math.floor(Math.log(bytes) / Math.log(k));
  return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];                                                                                                                    //return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

function readHistory(){
  console.log(sessionId, sessionType, latestMsgId);
  ipcRenderer.send('get-historyMsg', sessionId, sessionType, latestMsgId);
}
