const GROUP_TYPE_NORMAL   = 0x01;
const GROUP_TYPE_TMP      = 0x02;
const SESSION_TYPE_USER= 1;
const SESSION_TYPE_GROUP = 2;
const IMG_TYPE_GROUP = "./css/img/main/Groups.png";
const IMG_TYPE_DISCUSS = "./css/img/main/DiscussionGroups.png";
const IMG_TYPE_SESSION = "./css/img/main/session.png";
const ICON_LIST_DOWN = "./css/img/main/list_icon_down.png";
const ICON_LIST_UP = "./css/img/main/list_icon_up.png";
let myGroupLi = $("#myGroupLi");
let myDiscusLi = $("#myDiscusLi");

//更新用户信息
ipcRenderer.on('userinfo-reply', (event, arg) => {
  for (let item of arg) {
    if($("#user_name_" + item.userId)){
      $("#user_name_" + item.userId).html(item.nickName);
    }
    if($("#deptMember_name_" + item.userId)){
      $("#deptMember_name_" + item.userId).html(item.nickName);
    }
    if (item.avatar != ""){
      if($("#user_avar_" + item.userId)){
        $("#user_avar_" + item.userId).attr('src', item.avatar);
      }
      if($("#deptMember_avar_" + item.userId)){
        $("#deptMember_avar_" + item.userId).attr('src', item.avatar);
      }
    }
  }
})

 // 用户上线提醒;
ipcRenderer.on('userOnline-notifi', (event, arg) => {
  if($("#user_avar_" + arg)){
    $("#user_avar_" + arg).toggleClass("gray",false);
  }
  if($("#deptMember_avar_" + arg)){
    $("#deptMember_avar_" + arg).toggleClass("gray",false);
  }
})

function getNodeLi(nodeId, nodeName){
  let userLi = $("<li></li>");
  userLi.attr( "id", "li_group_"+ nodeId);
  let userA = $("<a href＝'#'></a>");
  let userRowDiv = $("<div></div>");
  userRowDiv.addClass("row");
  let userAvarDiv = $("<div></div>");
  userAvarDiv.addClass("col-xs-3 text-center");
  let userAvarImg= $('<img class="img-circle">')
  userAvarImg.attr("src", IMG_TYPE_SESSION);
  userAvarImg.toggleClass("gray",true);
  userAvarImg.attr( "id", nodeName+"_avar_"+nodeId);
  userAvarDiv.append(userAvarImg);
  userRowDiv.append(userAvarDiv);
  let userNameDiv = $("<div style='padding:0;line-height:35px;'></div>");
  userNameDiv.addClass("col-xs-9");
  userNameDiv.attr("id",nodeName+'_name_'+nodeId);
  userRowDiv.append(userNameDiv);
  userA.append(userRowDiv);
  userLi.append(userA);
  return userLi;
}

// create group html info
function createGroupHtml(item){
  let nodeLi = $("#li_group_"+ item.groupId);
  if(!nodeLi.length>0){
    nodeLi = getNodeLi(item.groupId, "groupPanel");
  }
  //let nodeLi = getNodeLi(item.groupId, "groupPanel");
  nodeLi.dblclick(function(){
     ipcRenderer.send('open-session', item.groupId, SESSION_TYPE_GROUP);
  });
  if(item.groupType == GROUP_TYPE_NORMAL){
    myGroupLi.after(nodeLi);
  } else{
  //if(item.groupType == GROUP_TYPE_TMP){
    myDiscusLi.after(nodeLi);
  }

  if($("#group_name_"+item.groupId)){
    $("#group_name_"+item.groupId).html(item.groupName);
  }
  if($("#groupPanel_name_"+item.groupId)){
    $("#groupPanel_name_"+item.groupId).html(item.groupName);
  }

  let groupPanelImg = $("#groupPanel_avar_"+item.groupId);
  if(groupPanelImg){
    groupPanelImg.toggleClass("gray",false);
    if(item.groupAvatar && item.groupAvatar.length > 0){
      groupPanelImg.attr('src', item.groupAvatar);
    } else{
      if(item.groupType == GROUP_TYPE_NORMAL){
        groupPanelImg.attr('src', IMG_TYPE_GROUP);
      }
      if(item.groupType == GROUP_TYPE_TMP){
        groupPanelImg.attr('src', IMG_TYPE_DISCUSS);
      }
    }
  }
}

// let deptMemberMap = new Map();
// // 显示部门中的成员
// function showDeptMembers(deptId, deptNameLi, sessionWindowsId){
//   if(deptNameLi.siblings().length == 0){
//     $.getJSON("http://192.168.0.228:9090/getUsersByDept/"+deptId, function(data) {
//       let userIdList = []
//       for(let key in data){
//         let userLi = getNodeLi(data[key].id, "deptMember");
//         if(sessionWindowsId && sessionWindowsId != undefined)
//         {
//           userLi.dblclick(function(){
//             userListUpdate(this);
//           });
//         }else{
//          // userLi.bind("click",openUserGroupInfo(data[key].id, SESSION_TYPE_USER));
//          userLi.dblclick(function(){
//            ipcRenderer.send('open-session', data[key].id, SESSION_TYPE_USER);
//          });
//         }
//         deptNameLi.after(userLi);
//         userIdList.push(data[key].id);
//       }
//       deptMemberMap.set(deptId, userIdList);
//       ipcRenderer.send("load-userOnlineStat", userIdList);
//     })
//   } else{
//     deptNameLi.siblings().show();
//     // TODO 用户在线状态更新；
//   }
// }

// // 获取所有的部门信息
// function getAllDept(sessionWindowsId=''){
//   $.getJSON("http://192.168.0.228:9090/getAllDept/", function(data) {
//     for(let key in data){
//       let deptUl = $("<ul></ul>");
//       deptUl.addClass('nav nav-lis');
//       let deptNameLi = $("<li></li>");
//       deptNameLi.addClass('nav-header');
//       let deptUpImg = $('<img>')
//       deptUpImg.attr('src',ICON_LIST_UP);
//       deptUpImg.attr('width', "9px");
//       deptUpImg.attr('height', "9px");
//       deptNameLi.append(deptUpImg);
//       deptNameLi.append(data[key]);
//       deptNameLi.bind("click",function(){
//         if (deptUpImg.attr('src') == ICON_LIST_UP){
//           deptUpImg.attr('src', ICON_LIST_DOWN);
//           showDeptMembers(key, deptNameLi,sessionWindowsId);
//         } else{
//           deptUpImg.attr('src', ICON_LIST_UP);
//           deptNameLi.siblings().hide();
//         }
//       });
//       deptUl.append(deptNameLi);
//
//       $("#deptDiv").append(deptUl);
//     }
//   });
// }

function groupBacClick(obj){
  if ($(obj).children("img").attr('src') == ICON_LIST_UP){
    $(obj).children("img").attr('src', ICON_LIST_DOWN);
    $(obj).siblings().show();
  }else{
    $(obj).children("img").attr('src', ICON_LIST_UP);
    $(obj).siblings().hide();
  }
}
