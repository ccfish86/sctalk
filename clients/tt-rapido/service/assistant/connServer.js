var net = require('net');
var helper = require('./helper.js');
var trayAction = require('../main/trayAction.js');

var connServer = function (ipaddr, sport, dataCallback){
  this.status = false;
  this.ipaddr = ipaddr;
  this.sport = sport;
  this.socket = new net.Socket();
  this.temp;
  this.templen = 0;
  this.pduLen = 0;

  this.socket.on('data',function(data){
    if(this.templen > 0){
      data.copy(this.temp,this.templen, 0, data.length)
      if (this.templen + data.length >= this.temp.length){
        dataCallback(this.temp);
        this.tmp = null;
        this.templen = 0;
      } else{
        this.templen = this.templen + data.length;
      }
      console.log("socket data connect!!!!");
    } else{
      let pduLen = data.readUInt32BE(0,0);
      if ( pduLen > data.length){
        this.temp = new Buffer(pduLen);
        data.copy(this.temp, 0 , 0, data.length);
        this.templen = data.length
      } else{
        dataCallback(data);
      }
    }
  });

  this.socket.on('error',function(error){
    console.log('error:'+error);
    // TODO 偶尔会出现异常，先注释
    // if(this.socket){
    //   this.socket.destory();
    // }
    global.mainWindow.send('offline-myself');
  });

  this.socket.on('close',function(){
    console.log('Connection closed');
    global.mainWindow.send('offline-myself');
  });
}

connServer.prototype.getStatus = function(){
  return this.status;
}

connServer.prototype.setStatus = function(status){
  this.status = status;
}

connServer.prototype.conn = function(){
  this.socket.connect(this.sport, this.ipaddr, function(){
    this.status = true;
  });
}

connServer.prototype.senddata = function(data){
  this.socket.write(data);
}

module.exports = connServer;
