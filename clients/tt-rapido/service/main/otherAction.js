var BaseDefine_pb = require('../pb/IM.BaseDefine_pb.js');

var Other_pb = require('../pb/IM.Other_pb.js');
var heartBeat = Other_pb['IMHeartBeat'];

var helper = require("../assistant/helper.js");
var ttPBHeader = require('../assistant/TTPBHeader');
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');

var heartBeatInterval = 5000;
var heartVaule = 3;

function startHeartBeat(){
  setInterval(function() {
    let heartBeatImp = new heartBeat();
    let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_OTHER,
            BaseDefine_pb.OtherCmdID.CID_OTHER_HEARTBEAT);
    tcpClientModule.sendPacket(ttpbHeader, heartBeatImp);
    if(heartVaule-- < 0){
      console.log("I'm offline!!!");
    }
  }, heartBeatInterval);
}

function doHeartBeat(imPdu){
  heartVaule =3;
}

function otherRoute(imPdu){
  var cmdId = imPdu.getCmdid();
  switch (imPdu.getCmdid()) {
  case BaseDefine_pb.OtherCmdID.CID_OTHER_HEARTBEAT:
    doHeartBeat(imPdu);
    break;
  default:
    console.log("BaseDefine_pb.OtherCmdID, 命令ＩＤ没有处理");
  }
};

exports.startHeartBeat = startHeartBeat;
exports.doHeartBeat = doHeartBeat;
exports.otherRoute = otherRoute;
