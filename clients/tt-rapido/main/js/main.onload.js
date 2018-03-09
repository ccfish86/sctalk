const {ipcRenderer} = nodeRequire('electron');

function replace_msgData(msgData){
  let reg = /\&\$\#\@\~\^\@\[\{\:(http):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])\:\}\]\&\$\~\@\#\@/g
  if (reg.test(msgData)){
    return "[图片]"
  }
  return msgData;
}

function getSesseionDiv(sessJson){
  let sessType = "";
  let itemLi = $('<li class="row"></li>')
  let itemA = $("<a href＝'#'></a>");
  let itemDiv =  $('<div class="row"></div>')
  let userAvarDiv = $("<div class='col-xs-3 text-right' style='padding-right:10px;line-height:35px;'></div>");
  let argvImg = $('<img class="img-circle">');

  console.info('sessJson:' + sessJson)

  if (sessJson.toSessionType==SESSION_TYPE_GROUP){
    sessType = "group";
    argvImg.attr('src', IMG_TYPE_GROUP);
  } else{
    sessType = "user";
    argvImg.toggleClass("gray",true);
    argvImg.attr('src', IMG_TYPE_SESSION);
  }
  argvImg.attr('id', sessType+"_avar_"+sessJson.toSessionId);
  userAvarDiv.append(argvImg);
  itemDiv.append(userAvarDiv);

  let centDiv = $('<div class="col-xs-9 mcell"></div>');
  centDiv.toggleClass("grays", false);
  let nameDivId =  sessType+'_name_'+sessJson.toSessionId;
  centDiv.append('<div id="'+ nameDivId +'" class="col-xs-8 mcell user_title"></div>');
  let lastTimeDiv = $('<div class="col-xs-4 mcell"></div>');
  lastTimeDiv.attr('id', sessType+"_time_"+sessJson.toSessionId);
  lastTimeDiv.css("font-size", 12);
  lastTimeDiv.css("color", "#999");
  lastTimeDiv.append(sessJson.msgTime)
  centDiv.append(lastTimeDiv);

  let lastContDiv = $('<div class="col-xs-9"></div>');
  lastContDiv.attr('id', sessType+"_msg_"+sessJson.toSessionId);
  lastContDiv.addClass("textflow");
  lastContDiv.css("margin-top", "7px");
  lastContDiv.css("margin-bottom", "0px");
  lastContDiv.css("padding-left", "4px");
  lastContDiv.css("padding-right", "4px");
  lastContDiv.css("font-size", 12);
  lastContDiv.css("color", "#999");
  lastContDiv.append(replace_msgData(sessJson.msgData));
  centDiv.append(lastContDiv);

  let newMsgCntSpan = $('<span class="badge"></span>');
  newMsgCntSpan.attr("id", sessType+'_span_'+sessJson.toSessionId);
  newMsgCntSpan.css("background-color","#ec0b0b");
  //newMsgCntSpan.append();
  let newMsgCntDiv = $('<div class="col-xs-3 text-right" style="padding-right:10px;"></div>');
  newMsgCntDiv.append(newMsgCntSpan);
  centDiv.append(newMsgCntDiv);
  itemDiv.append(centDiv)
  itemA.append(itemDiv);
  itemLi.append(itemA);
  itemLi.attr("id", sessType+'_li_'+sessJson.toSessionId);
  //双击事件
  itemLi.dblclick(function(){
    ipcRenderer.send('open-session', sessJson.toSessionId, sessJson.toSessionType);
  });
  return itemLi;
}

// function getNodeLi(nodeId, nodeName){
//   let userLi = $("<li></li>");
//   let userA = $("<a href='#'></a>");
//   let userRowDiv = $("<div></div>");
//   userRowDiv.addClass("row");
//   let userAvarDiv = $("<div></div>");
//   userAvarDiv.addClass("col-xs-3 text-right");
//   let userAvarImg= $('<img class="img-circle">')
//   userAvarImg.attr( "width", "42px");
//   userAvarImg.attr( "height", "42px");
//   userAvarImg.attr("src", IMG_TYPE_SESSION);
//   userAvarImg.toggleClass("gray",true);
//   userAvarImg.attr( "id", nodeName+"_avar_"+nodeId);
//   userAvarDiv.append(userAvarImg);
//   userRowDiv.append(userAvarDiv);
//   let userNameDiv = $("<div style='padding:0; line-height:42px;'></div>");
//   userNameDiv.addClass("col-xs-9");
//   userNameDiv.attr("id",nodeName+'_name_'+nodeId);
//   userRowDiv.append(userNameDiv);
//   userA.append(userRowDiv);
//   userLi.append(userA);
//   return userLi;
// }

function totalCntSub(cnt){
  let allUnreadMsgCnt = parseInt($('#allNewMsgSpan').html()) - parseInt(cnt);
  $('#allNewMsgSpan').html(allUnreadMsgCnt);
  if( allUnreadMsgCnt < 1){
    $('#allNewMsgSpan').hide();
  }
}

// function offline(){
//   console.log("offline!!");
//   ipcRenderer.send('offline-stat', 'todo')
// }

function loadMyinfo(event, myInfoJson){
  console.info('loadMyinfo' + myInfoJson)
  if(myInfoJson.nickName){
    $("#myNameSpan").html(myInfoJson.nickName);
  }
  if(myInfoJson.signInfo){
    $("#mySignDiv").html(myInfoJson.signInfo);
    $("#mySignDiv").attr("title",myInfoJson.signInfo);
  }
  if(myInfoJson.avatar){
    $("#myAvatImg").attr('src',myInfoJson.avatar);
  }
}

// 页面　onload
$(function(){
  $("#deptDiv").hide();
  $("#groupDiv").hide();
  $('#allNewMsgSpan').hide();
  // 加载个人信息;
  // let myInfo = ipcRenderer.sendSync('get-myinfo', 'todo');
  // let myJson = $.parseJSON(myInfo);
  // loadMyinfo(null, myJson);
  // $("#myGroupLi").children("img").attr('src', ICON_LIST_DOWN);
  // $("#myDiscusLi").children("img").attr('src', ICON_LIST_DOWN);
  // // ipcRenderer.send('get-groupInfo', 'todo')
  ipcRenderer.on('get-myinfo-rsp', (event,myInfo) => {
    console.info('ipcRenderer get-myinfo-rsp' )
    loadMyinfo(null, myInfo);
    $("#myGroupLi").children("img").attr('src', ICON_LIST_DOWN);
    $("#myDiscusLi").children("img").attr('src', ICON_LIST_DOWN);
  })
  ipcRenderer.send('get-myinfo', 'todo');
})
