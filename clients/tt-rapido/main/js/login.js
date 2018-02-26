var ipcRender = nodeRequire('electron').ipcRenderer;

var loadUserInfo = false;
//md5密码加密处理
function md5 (data) {
  let Buffer = nodeRequire("buffer").Buffer;
  let buf = new Buffer(data);
  let str = buf.toString("binary");
  let crypto = nodeRequire("crypto");
  return crypto.createHash("md5").update(str).digest("hex");
}

//用户登录
function login(){
  let userinput = $('#userNameInput').val();
  let passinput = $('#passwordInput').val();
  //验证账号
  if (userinput == ''){
    //将错误信息写到form_error这个div中
      $("#form_error").text('用户名不能为空');
      //光标定位到用户名输入框
      $("#userNameInput").focus();
      return false;
  }
  //验证密码
  if (passinput == ''){
    //将错误信息写到form_error这个div中
      $("#form_error").text('密码不能为空');
      //光标定位到密码输入框
      $("#passwordInput").focus();
      return false;
  }

  ipcRender.send('login-req', userinput, loadUserInfo?passinput:md5(passinput), $('#auto_login').is(':checked'),
      $('#rememberme_password').is(':checked'));
}

//回车键触发事件
$(document).keyup(function(event){
    if(event.keyCode ==13){
        login();
    }
});

//接收服务端返回的错误信息
ipcRender.on('login-error', (event, arg) => {
  //将错误信息写到form_error这个div中
  $("#form_error").text(arg);
  //强制清除用户名密码
  $('#userNameInput').val('');
  $('#passwordInput').val('');
  //光标定位到用户名输入框
  $("#userNameInput").focus();
})

// save username and password
ipcRender.on('load-userInfo', (event, username, password) => {
  $('#userNameInput').val(username);
  $('#passwordInput').val(password);
  $('#rememberme_password').attr("checked","checked");
  loadUserInfo = true;
})

//登录窗口最小化
function miniWindow(){
  ipcRender.send('mainWin-mini');
}

//关闭登录窗口
function closeWindow(){
  window.opener=null;
  window.open('','_self');
  window.close();
}
