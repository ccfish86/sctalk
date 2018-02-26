var http = require('http');
var constant = require('../../base/constant.js');
var tcpClientModule = require('./TcpClientModuleImpl.js');
var connServer = require('./connServer.js');

var options = {
  hostname:constant.request_addr,
  port:constant.request_port,
  path:constant.request_path,
  method:constant.reqest_method
}

var req = http.request(options, function (res) {
  res.setEncoding('utf8');
  res.on('data', function (resdata) {
    var resjson = JSON.parse(resdata);
    var priorIP = resjson['priorIP'];
    var backupIP = resjson['backupIP'];
    var port = resjson['port'];
    var msfsPrior=resjson['msfsPrior'];
    var msfsBackup=resjson['msfsBackup'];
    var discovery=resjson['discovery'];
    var code=resjson['code'];
    var msg=resjson['mss'];
  });
});

req.on('error', function (e) {
   console.log('problem with request: ' + e.message);
});

req.end();
