var http = require('http');
var fs = require('fs');
var util = require('util');
var path = require('path');
var querystring = require('querystring');
var config = require('./config.js');
var max = 9007199254740992;

var mimes = {
  '.png': 'image/png',
  '.gif': 'image/gif',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg'
};

var post_options = {
  host: config.msfsHost,
  port: config.msfsPort,
  path: config.msfsPath,
  method: config.msfsMethod,
};

function postPicture(fileName, rspCallback) {
  console.log('postPicture------------->');
  fs.readFile(fileName, function (err, imgData) {
    let ext = path.extname(fileName);
    let mime = mimes[ext];
    postByBuffer(imgData, fileName, mime, rspCallback);
  });
}

function postByNativeImg(nativeImagObj, rspCallback) {
  let dataBuffer = nativeImagObj.toJPEG(75);
  let sizeJson = nativeImagObj.getSize();
  let dec = Math.random() * max;
  let hex = dec.toString(36);
  fileName = hex+"_" + sizeJson.width + "x" + sizeJson.height + ".jpg"
  postByBuffer(dataBuffer, fileName, "image/jpeg", rspCallback);
}

function postByBuffer(imageBuffer, fileName, mime, rspCallback){
  let dec = Math.random() * max;
  let hex = dec.toString(36);
  let BOUNDARYPREFIX = '--rapido';
  let boundary = BOUNDARYPREFIX + hex;

  let bodyHead = util.format('--%s\r\n', boundary)
  bodyHead += util.format('Content-Disposition: form-data; name="image"; filename="%s"\r\n',fileName);
  bodyHead += util.format('Content-Type: %s\r\n\r\n', mime);
  let bodyTail = util.format('\r\n--%s--', boundary);
  let bodyLen =  Buffer.byteLength(bodyHead)
    + Buffer.byteLength(imageBuffer)
    + Buffer.byteLength(bodyTail);

  post_options.headers = {
    'Content-Type': 'multipart/form-data; boundary='+boundary,
    'Content-Length': bodyLen,
    'Connection': 'keep-alive'
  }

  let post_req = http.request(post_options, function(res) {
    res.setEncoding('utf8');
    if (res.statusCode == 200) {
      res.on('data', rspCallback);
    } else {
      // TODO 异常
    }
  });

  post_req.write(bodyHead);
  post_req.write(imageBuffer);
  post_req.write(bodyTail);
  post_req.end();
}

// var doRsp = function (chunk){
//   console.log('Response: ' + chunk);
// }
// postPicture("D:/work/考虑添加的表情/TB1.mYJHXXXXXXDXFXXgG2BJFXX-89-96.gif", doRsp);

exports.postByNativeImg = postByNativeImg;
exports.postPicture = postPicture;
