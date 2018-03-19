const {ipcRenderer} = require('electron');
let addTempGroupUserId = [];

$(function(){
  ipcRenderer.on('group-session-load', (event, sessionId, sessionType, tempGroupInfo, userInfo) => {
    let selfInfo = JSON.parse(userInfo);
    let sessionWindowsId  = sessionId + "_" + sessionType;
    let userObjcetList = tempGroupInfo.showGroupList;
    if(SESSION_TYPE_GROUP == sessionType){
      $("input[name='userGroupName']").val(tempGroupInfo.groupName).attr("disabled","disabled");
      $("input[name='createId']").val(tempGroupInfo.groupCreatorId).attr("disabled","disabled");
      $("input[type='file']").attr("disabled","disabled");
      if(tempGroupInfo.groupAvatar != ''){
        $("#groupAvatImg").css({"background-image":"url("+tempGroupInfo.groupAvatar+")"});
      }
      $("input[name='groupId']").val(tempGroupInfo.groupId);
    }else{
      addTempGroupUserId = [selfInfo['userId'],userObjcetList[0]['userId']];
    }

    let li = "<li userId='"+selfInfo['userId']+"'><img src='"+selfInfo['avatar']+"'>"
            +selfInfo['nickName']+"</li>";
    for (var value in  userObjcetList) {
      if(selfInfo['userId'] != userObjcetList[value]['userId']){
        li+="<li userId='"+userObjcetList[value]['userId']+"' id ='tempGroupUserId"
            +userObjcetList[value]['userId']+"'><img src='"+userObjcetList[value]['avatar']+"'>"
            +userObjcetList[value]['nickName'];
        li+="<span onclick='delTempGroupUser(this);' class='delTempGroupUser'> </span>";
        li+="</li>";
      }
    }

    $(".oldUserList").html('<ul>'+li+'</ul>');
    // getAllDept(sessionWindowsId);

    $("input[name='cancel']").click(function(){
      ipcRenderer.send('sessionGroupWin-close', sessionId, sessionType);
    })

    $("#closeWindow").click(function(){
      ipcRenderer.send('sessionGroupWin-close', sessionId, sessionType);
    });

    $("input[name='Submit']").click(function(){
      if(removeTempGroupUserId.length == 0 && addTempGroupUserId == 0){
        ipcRenderer.send('sessionGroupWin-close', sessionId, sessionType);
        return false;
      }

      //更新临时组信息
      let groupAvatar = '';
      console.log($("#sendUserPicfile")[0].files[0]);
      if(typeof($("#sendUserPicfile")[0].files[0]) != "undefined"){
        groupAvatar = $("#sendUserPicfile")[0].files[0].path;
      }
      let userId = $("input[name='createId']").val();
      //group_name
      let group_name = $("input[name='userGroupName']").val();
      if(group_name.trim() == ""){
        alert("讨论组名称不能为空！");
        return false;
      }

      //group_type
      let group_id = $("input[name='groupId']").val();
      let userIdList = []
      $(".oldUserList>ul>li").each(function(key,data){
        userIdList.push($(data).attr('userId'));
      });

      let updateTempGroupInfo = {
        "user_id":userId,
        'group_name':group_name,
        "group_id":group_id,
        "group_avatar":groupAvatar,
        "member_id_list":addTempGroupUserId,
        "remove_id_list":removeTempGroupUserId,
      };
      ipcRenderer.send('session-group-update', sessionId, sessionType,updateTempGroupInfo);
    })
  });
});

 let removeTempGroupUserId = [];

//update group user
function userListUpdate(Obj) {
  let regexp       = /\d+/gi;
  let imgObj       = $(Obj).find('img');
  let strId        = imgObj.attr("id");
  let rs           = strId.match(regexp);
  let userListLi   = $(".oldUserList>ul>li");
  let userImgLocal = imgObj.attr("src");
  let name         = $("#deptMember_name_"+rs[0]).text();
  let addFlag      = false;
  userListLi.each(function(key,value){
    if ($(value).attr('userId')  == rs[0]){
      addFlag = true;
    }
  });

  if(!addFlag) {
    $(".oldUserList>ul").append("<li userId ='"+rs[0]+"'><img src='"+userImgLocal+"'>"
      +name+" <span onclick='delTempGroupUser(this);' class='delTempGroupUser'></span></li>");
    let pos = removeTempGroupUserId.indexOf(rs[0]);

    if(-1 != pos){
      removeTempGroupUserId.splice(pos, 1);
    }else{
      addTempGroupUserId.push(rs[0]);
    }
  }
}

function delTempGroupUser(userId) {
  let delUserId = $(userId).parent("li").attr("userId");
  $(userId).parent("li").remove();
  var pos = addTempGroupUserId.indexOf(delUserId);
  if(-1 != pos){
    addTempGroupUserId.splice(pos, 1);
  }else{
    removeTempGroupUserId.push(delUserId);
  }
}

function sendUserPicture(obj){
  var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
  let filesPath = obj.files[0].path
  if(isWin){
    filesPath = filesPath.replace(/\\/gi, "/");
  }
  $("#groupAvatImg").css({"background-image":"url('"+filesPath+"')"});
}

//close group window
