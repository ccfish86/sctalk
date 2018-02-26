var net = require('net');
var constant = require('./constant.js');
var connServer = require('./connServer.js');
var mRoute = require('../main/imPduRouter.js');
var config = require('../assistant/config.js');
var priorConn;

exports.proiorInit = function(){
  priorConn = new connServer(config.msgHost, config.msgPort, mRoute.imPduRoute);
  priorConn.conn();
}

exports.sendPacket = function(ttPbHeader, ttPbBody){
  var arryPbBody = ttPbBody.serializeBinary();
  var dataLen = constant.HEADER_LENGTH + arryPbBody.length;
  ttPbHeader.setLen(dataLen);
  var buf = new Buffer(dataLen);
  ttPbHeader.getBuf().copy(buf, 0);
  new Buffer(arryPbBody).copy(buf, 16);
  priorConn.senddata(buf)
}
