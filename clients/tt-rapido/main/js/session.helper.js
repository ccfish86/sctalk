//查看结果
function replace_em(message){
  let facepatt = new RegExp(faceRegStr,"g");
  if(message){
    message.content = message.content.replace(/\</g,'&lt;');
    message.content = message.content.replace(/\>/g,'&gt;');
    let withImg = false;
    let reg = /\&\$\#\@\~\^\@\[\{\:(http):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])\:\}\]\&\$\~\@\#\@/g
    message.content = message.content.replace(reg, function(matchStr){
      let imgUrl = matchStr.replace("&$#@~^@[{:","");
      imgUrl = imgUrl.replace(":}]&$~@#@", "");
      let filesPath = imgUrl.replace(/\\/gi, "/");
      console.log(filesPath);
      // TODO 处理下图片特别宽的情况
      return  '<img src="'+imgUrl+'" onclick="windowsImgView(this)">';
    });

    //str = str.replace(/\n/g,'<br/>');
    message.content= message.content.replace(facepatt,function(face){
      if(face){
        return '<img src="css/img/session/Face/'+faceJson[face]+'">'
      } else{
        return face;
      }
    });
  }
  return message;
}

function getMsgJson(nickName, userId, msgtime, userAvatar, msgType, msgData, mtype){
  var message = {
    name: nickName,
    userid: userId,
    time: msgtime,
    avatar: userAvatar,
    msgtype: msgType,
    content: msgData,
    mtype: mtype,
    uuid: '1111111111111'
  };
  return replace_em(message);
}
