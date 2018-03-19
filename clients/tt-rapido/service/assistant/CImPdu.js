var CImPdu = function(data){
  var pdu = Buffer(data);
  this.len = pdu.readUInt32BE(0, 4);
  this.head_version = pdu.readUInt16BE( 4, 2);
  this.head_falg = pdu.readUInt16BE(6, 2);
  this.moduid = pdu.readUInt16BE(8, 2);
  this.cmdid = pdu.readUInt16BE(10, 2);
  this.seqnum = pdu.readUInt16BE(12, 2);
  this.reserd = pdu.readUInt16BE(14, 2);
  pbbodybf = new Buffer(this.len - 16);
  pdu.copy(pbbodybf, 0, 16, this.len);
  this.pbbody = pbbodybf;
}

CImPdu.prototype.getSeqNum = function(){
  return this.seqnum;
}

CImPdu.prototype.getModuid = function(){
  return this.moduid;
}

CImPdu.prototype.getCmdid = function(){
  return this.cmdid;
}

CImPdu.prototype.getPbBody = function(){
  return this.pbbody;
}

module.exports = CImPdu;
