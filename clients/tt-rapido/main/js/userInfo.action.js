var ipcRender = nodeRequire('electron').ipcRenderer;

//自动获取个人信息
$(function(){
  ipcRender.on('user-info-load', (event, arg) => {
      var gender = arg['gender'];
      $("#userId").val(arg['userId']);
      $("#userImg").css("background-image",'url("'+arg['avatar']+'")');
      $("#sendUserPicfile").attr('title',arg['userId']);
      $("#nickName").text(arg['nickName']);
      //性别
      if(gender == 1){
         $("#gender").text('帅哥');
      }else if(gender == 0){
         $("#gender").text('美女');
      }else{
        $("#gender").text('未知');
      }
      $("#department").text(arg['department']);
      $("#tel").val(arg['tel']);
      $("#email").val(arg['email']);
      $("#signInfo").val(arg['signInfo']);

      $("#closeWindow").click(function(){
        ipcRender.send('sessionMyWin-close', arg['userId']);
      });

      $("#closeButton").click(function(){
        ipcRender.send('sessionMyWin-close',arg['userId']);
      });

  });
})

//提交个人资料
function editUserInfo(){
  let userId = $('#userId').val();
  //验证电话号码
  let phone = $("#tel").val();
  if(phone == ''){
    $("#error_info").html("<font color='red'>电话号码不能为空，请输入！</font>");
    $("#tel").focus();
    return false; 
  }
  if(isNaN(phone)){
    $("#error_info").html("<font color='red'>电话号码必须为11位数字，请重新输入！</font>");
    $("#tel").focus();
    return false; 
  }
  //验证邮箱
  let email = $('#email').val();
  if(email == ''){
    $("#error_info").html("<font color='red'>邮箱不能为空，请输入！</font>");
    $("#tel").focus();
    return false; 
  }
  if(!email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
    $("#error_info").html("<font color='red'>邮箱格式不正确，请重新输入！</font>");
    $("#email").focus(); 
    return false; 
  } 
  let signInfo = $('#signInfo').val();
  let avatar='';
  if(typeof($("#avatar")[0].files[0]) != "undefined"){
    avatar = $("#avatar")[0].files[0].path;
  }
  ipcRender.send('user-edit', userId, phone, email, signInfo, avatar);
}

//上传个人头像
function sendUserPicture(obj){
  var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
  let filesPath = obj.files[0].path
  if(isWin){
    filesPath = filesPath.replace(/\\/gi, "/");
  }
  $("#userImg").css({"background-image":"url('"+filesPath+"')"});
}
