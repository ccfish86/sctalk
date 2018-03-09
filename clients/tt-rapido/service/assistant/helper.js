var crypto = require('crypto')
var key = new Buffer("12345678901234567890123456789012")
var iv = "";
var clearEncoding = 'utf8';
var cipherEncoding = 'base64';
var enc = 'aes-256-ecb'
var seqNum = 1;
var moment = require('moment');
moment.locale('zh-cn');
var net = require('net');
var constant = require('./constant.js');

// 管理seq，处理回调
var seqNumMap = new Map();

// TOTO 添加日志模块
// var log4js = require('log4js');
// log4js.configure("service/assistant/log4js.json");
// var logInfo = log4js.getLogger('logInfo');

var ttPBHeader = require('../assistant/TTPBHeader');

function buffer2arr(buf){
  let arr = new Array();
  for (let i = 0; i < buf.length; i++) {
      arr.push(buf[i].toString(16));
  }
  return arr.join(' ');
}

//data 是准备加密的字符串,key是你的密钥
function encryption(data){
    let cipherChunks = [];
    let cipher = crypto.createCipheriv(enc, key, iv);
    cipher.setAutoPadding(false);
    let nRemain = data.length%16;
    let nBlocks = Math.floor(data.length/16);
    if (nRemain > 12 || nRemain == 0) {
      nBlocks = nBlocks+1
    }
    for (let i = 0; i<=nBlocks; i++){
      let tmpBuffer = new Buffer(16);
      tmpBuffer.fill(0);
      if (i == nBlocks){
        if (nRemain <= 12 && nRemain != 0) {
          data.copy(tmpBuffer,0, i*16,(i+1)*16);
        }
        tmpBuffer.writeUInt32BE(data.length, 12);
      }else{
        data.copy(tmpBuffer,0, i*16,(i+1)*16);
      }
      // 最后
      tmpBuffer.writeInt32BE(tmpBuffer.length - 4, data.length)
      cipherChunks.push(cipher.update(tmpBuffer, clearEncoding, cipherEncoding));
    }

    //cipherChunks.push(cipher.update(data, clearEncoding, cipherEncoding));
    cipherChunks.push(cipher.final(cipherEncoding));

    return cipherChunks.join('');
}

//data 是你的准备解密的字符串,key是你的密钥
function decryption(data){
    let cipherChunks = [];
    let decipher = crypto.createDecipheriv(enc, key, iv);
    decipher.setAutoPadding(false);
    // let clearEncData = new Buffer(data, 'base64');
    // let nBlocks = clearEncData.length/16;
    //
    // for (let i = 0; i<nBlocks; i++){
    //   let tmpBuffer = new Buffer(16)
    //   tmpBuffer.fill(0)
    //   clearEncData.copy(tmpBuffer,0, i*16,(i+1)*16);
    //   cipherChunks.push(decipher.update(tmpBuffer, cipherEncoding, clearEncoding));
    // }
    cipherChunks.push(decipher.update(data, cipherEncoding, clearEncoding));
    cipherChunks.push(decipher.final(clearEncoding));
    // 解密
    let decryStr = cipherChunks.join('');
    // 取正文长度(后4位)
    let aa = new Buffer(decryStr, "utf8")
    let len = aa.readInt32BE(aa.length - 4)
    let dstr = decryStr.substr(0,len);
    // console.info('aes %s, %d, %d',dstr, dstr.length, len)
    return dstr
    //return decryStr;
}

// TODO seqNum too big too bear
function getSeqNum(){
  seqNum = seqNum + 1;
  return seqNum
}

function u8arrToStr(Uint8Arr){
   return String.fromCharCode.apply(null, Uint8Arr);
}

function u8arrToDecry(Uint8Arr){
  let data = String.fromCharCode.apply(null, Uint8Arr);
  return decryption(data);
}

function getPBHeader(ServiceID,CmdID){
  seqNum = seqNum + 1;
  return new ttPBHeader(ServiceID, CmdID, seqNum);
}

function getDateStr(datetimeUnix){
  let dateObj = moment.unix(datetimeUnix)
  if(moment().isSame(dateObj, 'day')){
    return dateObj.format("H:mm");;
  }else{
    return dateObj.format("MM-DD");;
  }
}

function addCallback(seqNum, callback){
  console.warn('addCallback: ' + seqNum);
  seqNumMap.set(seqNum, callback);
}

function doCallback(seqNum, ...values){
  console.info('doCallback: ' + seqNum);
  if(seqNumMap.has(seqNum)){
    seqNumMap.get(seqNum)(...values);
    seqNumMap.delete(seqNum);
  }
}

function getSendBuf(ttPbHeader, ttPbBody){
  var arryPbBody = ttPbBody.serializeBinary();
  var dataLen = constant.HEADER_LENGTH + arryPbBody.length;
  ttPbHeader.setLen(dataLen);
  var buf = new Buffer(dataLen);
  ttPbHeader.getBuf().copy(buf, 0);
  new Buffer(arryPbBody).copy(buf, 16);
  return buf;
}

exports.encryption = encryption;
exports.u8arrToStr = u8arrToStr;
exports.u8arrToDecry = u8arrToDecry;
exports.getPBHeader = getPBHeader;
exports.getDateStr = getDateStr;
exports.buffer2arr = buffer2arr;
exports.getSendBuf = getSendBuf;

exports.addCallback = addCallback;
exports.doCallback = doCallback;
