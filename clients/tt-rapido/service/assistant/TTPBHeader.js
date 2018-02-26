var constant = require('./constant.js');

var ttPBHeader = function(moduleId, cmdId, seq){
  this.seqNum = seq;
  this.buf = new Buffer(constant.HEADER_LENGTH);
  this.buf.writeUInt32BE(0, 0, 4);
  this.buf.writeUInt16BE(constant.HEADER_VERSION, 4, 2);
  this.buf.writeUInt16BE(constant.HEADER_FLAG, 6, 2);
  this.buf.writeUInt16BE(moduleId, 8, 2);
  this.buf.writeUInt16BE(cmdId, 10, 2);
  this.buf.writeUInt16BE(seq, 12, 2);
  this.buf.writeUInt16BE(0, 14, 2);
}

ttPBHeader.prototype.getBuf = function(){
  return this.buf;
}

ttPBHeader.prototype.getSeqNum = function(){
  return this.seqNum;
}

ttPBHeader.prototype.setLen = function(len){
  this.buf.writeUInt32BE(len, 0, 4);
}

module.exports = ttPBHeader;
