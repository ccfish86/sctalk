$(function () {
  //更新用户信息
  ipcRenderer.on('userinfo-reply', (event, arg) => {
    for (let item of arg
      ) {
      if ($("#user_name_" + item.userId)) {
        $("#user_name_" + item.userId).html(item.nickName);
      }
      if ($("#deptMember_name_" + item.userId)) {
        $("#deptMember_name_" + item.userId).html(item.nickName);
      }
      if (item.avatar != "") {
        if ($("#user_avar_" + item.userId)) {
          $("#user_avar_" + item.userId).attr('src', item.avatar);
        }
        if ($("#deptMember_avar_" + item.userId)) {
          $("#deptMember_avar_" + item.userId).attr('src', item.avatar);
        }
      }
    }
  })

  // 获取最近联系人列表;
  ipcRenderer.on('session-reply', (event, arg) => {
    let sessionLi = $("#mysessionLi");
    for (let i = arg.length - 1; i >= 0; i--) {
      let item = arg[i];
      sessionLi.after(getSesseionDiv(item));
    }
  })

  // 用户上线提醒;
  ipcRenderer.on('userOnline-notifi', (event, arg) => {
    // console.log('userOnline-notifi', arg);
    if ($("#user_avar_" + arg)) {
      $("#user_avar_" + arg).toggleClass("gray", false);
    }
    if ($("#user_name_" + arg)) {
      $("#user_name_" + arg).toggleClass("grays", false);
    }
    if ($("#deptMember_avar_" + arg)) {
      $("#deptMember_avar_" + arg).toggleClass("gray", false);
    }
  })

  // 用户下线提醒;
  ipcRenderer.on('userOffline-notifi', (event, arg) => {
    // console.log('userOnline-notifi', arg);
    let argvImg = $("#user_avar_" + arg);
    let user_name = $("#user_name_" + arg);
    if (argvImg) {
      argvImg.toggleClass("gray", true);
    }
    if (user_name) {
      user_name.toggleClass("grays", true);
    }
  })

  // 获取组群信息;
  ipcRenderer.on('groupinfo-reply', (event, arg) => {
    for (let item of arg
      ) {
      createGroupHtml(item);
    }
  })

  // 添加组群信息;
  ipcRenderer.on('groupinfo-add', (event, item) => {
    // console.log("item:", item);
    createGroupHtml(item);
  })
  // 增加全部未读消息数
  ipcRenderer.on('allMsgCnt-add', (event, arg) => {
    // console.log("allMsgCnt-add:", arg);
    let allUnreadMsgCnt = 0;
    if ($('#allNewMsgSpan').html()) {
      allUnreadMsgCnt = parseInt($('#allNewMsgSpan').html());
    }
    allUnreadMsgCnt = allUnreadMsgCnt + arg;
    $('#allNewMsgSpan').html(allUnreadMsgCnt);
    if (allUnreadMsgCnt > 0) {
      $('#allNewMsgSpan').show();
    }
  })

  // 减小全部未读消息数
  ipcRenderer.on('allMsgCnt-sub', (event, arg) => {
    totalCntSub(arg);
  })

  // 增加新消息数
  ipcRenderer.on('itemNewMsg-add', (event, arg) => {
    for (let item of arg
      ) {
      recentSessUpate(arg[arg.length - 1]);
      if (!item.toSessionId) {
        alert("找不到对象：)！！！！！");
      }
      let spanCntObj, timeMsgObj, divMsgObj, liItemObj;
      if (item.toSessionType == SESSION_TYPE_GROUP) {
        spanCntObj = $('#group_span_' + item.toSessionId);
      } else {
        spanCntObj = $('#user_span_' + item.toSessionId);
      }

      if (spanCntObj) {
        let unreadMsgCnt = 0;
        if (item.unreadCnt) {
          unreadMsgCnt = parseInt(item.unreadCnt);
        } else {
          if (spanCntObj.html()) {
            unreadMsgCnt = parseInt(spanCntObj.html());
          }
          unreadMsgCnt = unreadMsgCnt + 1;
        }
        spanCntObj.html(unreadMsgCnt);
      }
    }
  })

  // 收到新消息
  ipcRenderer.on('itemNewMsg-update', (event, arg) => {
    recentSessUpate(arg);
  })

  // 清除新消息
  ipcRenderer.on('itemNewMsg-clear', (event, arg) => {
    let sessObj;
    if (arg.toSessionType == SESSION_TYPE_GROUP) {
      sessObj = $('#group_span_' + arg.toSessionId);
    } else {
      sessObj = $('#user_span_' + arg.toSessionId);
    }
    if (sessObj) {
      console.log("sessObj.html():", sessObj.html());
      if (sessObj.html()) {
        totalCntSub(sessObj.html());
      }
      sessObj.html("");
    }
  })

  // 获取部门信息
  // IMDepartmentReq
  ipcRenderer.on('dept-reply', (event, arg) => {
    // 处理部门显示
    for (let key in arg) {
      let deptUl = $("<ul></ul>");
      deptUl.addClass('nav nav-lis');
      let deptNameLi = $("<li></li>");
      deptNameLi.addClass('nav-header');
      let deptUpImg = $('<img>')
      deptUpImg.attr('src', ICON_LIST_UP);
      deptUpImg.attr('width', "9px");
      deptUpImg.attr('height', "9px");
      deptNameLi.append(deptUpImg);
      deptNameLi.append(arg[key]);
      deptNameLi.bind("click", function () {
        if (deptUpImg.attr('src') == ICON_LIST_UP) {
          deptUpImg.attr('src', ICON_LIST_DOWN);
          showDeptMembers(key, deptNameLi);
        } else {
          deptUpImg.attr('src', ICON_LIST_UP);
          deptNameLi.siblings().hide();
        }
      });
      deptUl.append(deptNameLi);

      $("#deptDiv").append(deptUl);
    }
  })

  // 窗口抖动
  ipcRenderer.on('shake-window', (event, arg) => {
    // 打开聊天窗口
    if (arg) {
      console.log('sessionWindow 抖动')
      ipcRenderer.send('open-session', arg, SESSION_TYPE_USER, (sessionWindow) => {
        // FIXME: 抖动
        console.log('sessionWindow 抖动')
        console.log(sessionWindow)
      });
    }
  })

  // 更新个人信息
  ipcRenderer.on('update-myInfo', loadMyinfo);
  ipcRenderer.on('offline-myself', Offline);

})

let deptMemberMap = new Map();
function recentSessUpate(recentMsgJosn) {
  // console.log("recentMsgJosn:", recentMsgJosn);
  let timeMsgObj, divMsgObj, liItemObj;
  if (recentMsgJosn.toSessionType == SESSION_TYPE_GROUP) {
    timeMsgObj = $('#group_time_' + recentMsgJosn.toSessionId);
    divMsgObj = $('#group_msg_' + recentMsgJosn.toSessionId);
    liItemObj = $('#group_li_' + recentMsgJosn.toSessionId);
  } else {
    timeMsgObj = $('#user_time_' + recentMsgJosn.toSessionId);
    divMsgObj = $('#user_msg_' + recentMsgJosn.toSessionId);
    liItemObj = $('#user_li_' + recentMsgJosn.toSessionId);
  }

  if (liItemObj.length > 0) {
    $("#mysessionLi").after(liItemObj);
  } else {
    $("#mysessionLi").after(getSesseionDiv(recentMsgJosn));
  }
  if (timeMsgObj && recentMsgJosn.msgTime) {
    timeMsgObj.html(recentMsgJosn.msgTime);
  }

  if (divMsgObj && recentMsgJosn.msgData) {
    divMsgObj.html(replace_msgData(recentMsgJosn.msgData));
  }
}

function showDeptMembers(deptId, deptNameLi, sessionWindowsId = '') {
  if(deptNameLi.siblings().length == 0){
    // TODO 部门员工查询
    ipcRenderer.send('dept-mem', deptId)
    ipcRenderer.on('dept-mem-reply', (event, data) => {
      let userIdList = []
      for (let key in data) {
        let userLi = getNodeLi(data[key].userId, "deptMember");
        if (sessionWindowsId && sessionWindowsId != undefined) {
          userLi.dblclick(function () {
            userListUpdate(this);
          });
        } else {
          // userLi.bind("click",openUserGroupInfo(data[key].id, SESSION_TYPE_USER));
          userLi.dblclick(function () {
            ipcRenderer.send('open-session', data[key].userId, SESSION_TYPE_USER);
          });
        }
        deptNameLi.after(userLi);
        userIdList.push(data[key].userId);
      }
      deptMemberMap.set(deptId, userIdList);
      ipcRenderer.send("load-userOnlineStat", userIdList);
    })
  } else{
    deptNameLi.siblings().show();
    // TODO 用户在线状态更新；
  }
}
