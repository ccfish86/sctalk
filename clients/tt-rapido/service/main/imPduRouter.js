var CImPdu = require('../assistant/CImPdu.js');
var BaseDefine = require('../pb/IM.BaseDefine_pb.js');

var loginAction = require('./loginAction.js');
var buddyAction = require('./buddyAction.js');
var groupAction = require('./groupAction.js');
var messgAction = require('./messgAction.js');
var otherAction = require('./otherAction.js');
var fileAction = require('./fileAction.js');


exports.imPduRoute = function(data){
  var imPdu = new CImPdu(data);
  switch (imPdu.getModuid()){
    case BaseDefine.ServiceID.SID_LOGIN:
      loginAction.loginRoute(imPdu);
      break;
    case BaseDefine.ServiceID.SID_BUDDY_LIST:
      buddyAction.buddyListRoute(imPdu);
      break;
    case BaseDefine.ServiceID.SID_MSG:
      messgAction.messgRoute(imPdu);
      break;
    case BaseDefine.ServiceID.SID_GROUP:
      groupAction.groupRoute(imPdu);
      break;
    case BaseDefine.ServiceID.SID_FILE:
      fileAction.fileRoute(imPdu);
      break;
    case BaseDefine.ServiceID.SID_OTHER:
      otherAction.otherRoute(imPdu);
      break;
    default:
      console.log("imPduRouter:", imPdu.getModuid(),BaseDefine.ServiceID.SID_OTHER);
  }
  return imPdu
}
