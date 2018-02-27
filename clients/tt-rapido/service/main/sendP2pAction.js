var exec = require('child_process').exec;
var helper = require("../assistant/helper.js");

var SwitchService_pb = require('../pb/IM.SwitchService_pb.js');
var BaseDefine_pb = require("../pb/IM.BaseDefine_pb.js");
var tcpClientModule = require('../assistant/TcpClientModuleImpl.js');
var P2PCmdMsg = SwitchService_pb['IMP2PCmdMsg'];

//P2P消息类型枚举
var P2PCMD_ENUM = {
  KEY_P2PCMD_SHAKEWINDOW: 1, //震屏
  KEY_P2PCMD_WRITING: 2, //正在输入
  KEY_P2PCMD_INNERMSG: 3, //内网推送的消息
};

function screenShake(userId) {
  let p2pDataObj = new P2PCmdMsg();
  p2pDataObj.setFromUserId(global.myUserId);
  p2pDataObj.setToUserId(userId);
  p2pDataObj.setCmdMsgData(JSON.stringify({
    service_id: P2PCMD_ENUM.KEY_P2PCMD_SHAKEWINDOW,
    cmd_id: P2PCMD_ENUM.KEY_P2PCMD_SHAKEWINDOW << 16 |1,
    content: 'shakewindow'
  }));

  console.log('send CmdMsgData:' + p2pDataObj.getCmdMsgData());
  let ttpbHeader = helper.getPBHeader(BaseDefine_pb.ServiceID.SID_SWITCH_SERVICE,
    BaseDefine_pb.SwitchServiceCmdID.CID_SWITCH_P2P_CMD);
  tcpClientModule.sendPacket(ttpbHeader, p2pDataObj);
}
function p2pRoute(imPdu) {
  console.info('p2p2 route')
  switch (imPdu.getCmdid()) {
    case BaseDefine_pb.SwitchServiceCmdID.CID_SWITCH_P2P_CMD:
      // 处理P2p消息
      // TODO 暂时仅处理shakewindow
      let p2pDataObj = P2PCmdMsg.deserializeBinary(new Uint8Array(imPdu.getPbBody()));
      let fromUserId = p2pDataObj.getFromUserId();
      let msgData = JSON.parse(p2pDataObj.getCmdMsgData());

      if (msgData.service_id === P2PCMD_ENUM.KEY_P2PCMD_SHAKEWINDOW) {
        global.mainWindow.send('shake-window', fromUserId);
      } else if (msgData.service_id === P2PCMD_ENUM.KEY_P2PCMD_WRITING){
        // TODO
        console.info(p2pDataObj.getCmdMsgData())
      } else {
        // TODO
        console.info(p2pDataObj.getCmdMsgData())
      }
      break;
    default:
      console.log("BaseDefine_pb.MessageCmdID, 命令ＩＤ没有处理:", imPdu.getCmdid());
  }
}
exports.P2PCMD_ENUM = P2PCMD_ENUM;
exports.screenShake = screenShake;
exports.p2pRoute = p2pRoute;