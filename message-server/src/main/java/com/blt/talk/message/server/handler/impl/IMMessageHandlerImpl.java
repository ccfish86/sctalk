/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.blt.talk.common.code.DefaultIMHeader;
import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.PduAttachData;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MsgType;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.SessionType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserTokenInfo;
import com.blt.talk.common.code.proto.IMBuddy.IMUsersStatReq;
import com.blt.talk.common.code.proto.IMMessage;
import com.blt.talk.common.code.proto.IMMessage.IMMsgData;
import com.blt.talk.common.code.proto.IMServer.IMPushToUserReq;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.constant.AttachType;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.constant.MessageConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.GroupPushEntity;
import com.blt.talk.common.model.entity.ShieldStatusEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.param.ClearUserCountReq;
import com.blt.talk.common.param.GroupMessageSendReq;
import com.blt.talk.common.param.MessageSendReq;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.common.util.SecurityUtils;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.handler.IMMessageHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
import com.blt.talk.message.server.remote.GroupService;
import com.blt.talk.message.server.remote.IphonePushService;
import com.blt.talk.message.server.remote.LoginService;
//import com.blt.talk.message.server.manager.ClientConnection;
//import com.blt.talk.message.server.manager.ClientConnectionMap;
import com.blt.talk.message.server.remote.MessageService;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理【MessageCmdID】
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class IMMessageHandlerImpl extends AbstractUserHandlerImpl implements IMMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private IphonePushService iphonePushService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMMessageHandler#sendMessage(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void sendMessage(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
       
        // 发送消息
        IMMessage.IMMsgData msgdata = (IMMessage.IMMsgData) body;
        
        // 获取用户ID/并更>FromUserId
        // 更新：消息时间
        long userId = super.getUserId(ctx);
        msgdata = msgdata.toBuilder().setFromUserId(userId).setCreateTime(CommonUtils.currentTimeSeconds()).build();
        
        // 消息长度，为空时，忽略
        if (msgdata.getMsgData().isEmpty()) {
            logger.debug("空消息，忽略");
            return;
        }
        
        // FIXME 限制用户一秒钟发送超20条
        // >
        
        // 判断是MSG_TYPE是否是同一用户
        if (msgdata.getFromUserId() == msgdata.getToSessionId() && CommonUtils.isMessageTypeSinble(msgdata.getMsgType())) {
            logger.debug("同一用户互发消息，忽略");
            return;
        }

        // 同时判断是个人信息还是群信息，以区分处理
        // MessageContent#sendMessage
        try {

            int time = CommonUtils.currentTimeSeconds();

            MsgType messageType = msgdata.getMsgType();
            if (messageType == MsgType.MSG_TYPE_GROUP_TEXT) {

                // 发送消息
                // - sendMessage to group
                GroupMessageSendReq messageReq = new GroupMessageSendReq();
                messageReq.setUserId(msgdata.getFromUserId());
                messageReq.setGroupId(msgdata.getToSessionId());
                messageReq.setCreateTime(time);
                messageReq.setMsgType(messageType);
                messageReq.setMsgContent(msgdata.getMsgData().toStringUtf8());

                BaseModel<Long> messageIdRes =  messageService.sendMessage(messageReq);

                msgdata = msgdata.toBuilder().setMsgId(messageIdRes.getData()).build();
            } else if (messageType == MsgType.MSG_TYPE_GROUP_AUDIO) {

                // 发送消息
                // - sendMessage to group
                GroupMessageSendReq messageReq = new GroupMessageSendReq();
                messageReq.setUserId(msgdata.getFromUserId());
                messageReq.setGroupId(msgdata.getToSessionId());
                messageReq.setCreateTime(time);
                messageReq.setMsgType(messageType);
                messageReq.setContent(msgdata.getMsgData().toByteArray());

                BaseModel<Long> messageIdRes =  messageService.sendMessage(messageReq);

                msgdata = msgdata.toBuilder().setMsgId(messageIdRes.getData()).build();
                // 群组语音
            } else if (messageType == MsgType.MSG_TYPE_SINGLE_TEXT) {
                
                if (msgdata.getFromUserId() == msgdata.getToSessionId()) {
                    logger.warn("不要跟自己聊");
                    return;
                }

                // sendMessage(nFromId, nToId, nMsgType, nCreateTime, nMsgId,
                // (string&)msg.msg_data())
                MessageSendReq messageReq = new MessageSendReq();
                messageReq.setUserId(msgdata.getFromUserId());
                messageReq.setToId(msgdata.getToSessionId());
                messageReq.setCreateTime(time);
                messageReq.setMsgType(messageType);
                messageReq.setMsgContent(msgdata.getMsgData().toStringUtf8());

                BaseModel<Long> messageIdRes = messageService.sendMessage(messageReq);
                msgdata = msgdata.toBuilder().setMsgId(messageIdRes.getData()).build();
            } else if (messageType == MsgType.MSG_TYPE_SINGLE_AUDIO) {
                
                MessageSendReq messageReq = new MessageSendReq();
                messageReq.setUserId(msgdata.getFromUserId());
                messageReq.setToId(msgdata.getToSessionId());
                messageReq.setCreateTime(time);
                messageReq.setMsgType(messageType);
                messageReq.setContent(msgdata.getMsgData().toByteArray());
                
                // 个人语音
                BaseModel<Long> messageIdRes = messageService.sendMessage(messageReq);
                msgdata = msgdata.toBuilder().setMsgId(messageIdRes.getData()).build();
            } else {

                // 暂不支持
                logger.warn("暂不支持的消息类型");
            }
            
            // 处理完消息保存后
            // CDBServConn::_HandleMsgData(CImPdu *pPdu)
            if (CommonUtils.isGroup(messageType)) {
                handleGroupMessage(header, msgdata, ctx);
            } else if (CommonUtils.isSingle(messageType)) {
                handleSingleMessage(header, msgdata, ctx);
            } else {
                // 忽略
            }

        } catch (Exception e) {
            logger.error("消息发送失败", e);
        } finally {
            // 消息送达，接收反馈(已分别处理过)
        }
    }

    /**
     * 处理私消息
     * 
     * @param header
     * @param msgdata
     * @since  1.0
     */
    private void handleSingleMessage(IMHeader header, IMMsgData msgdata, ChannelHandlerContext ctx) {
        if (msgdata.getMsgId() == DBConstant.INVALIAD_VALUE) {
            logger.warn("Write message to db failed, {}->{}.", msgdata.getFromUserId(), msgdata.getToSessionId());
            return ;
        }
        // 消息送达，接收反馈
        IMMessage.IMMsgDataAck.Builder messageDataAckBuilder = IMMessage.IMMsgDataAck.newBuilder();
        messageDataAckBuilder.setMsgId(msgdata.getMsgId());
        messageDataAckBuilder.setUserId(msgdata.getFromUserId());
        messageDataAckBuilder.setSessionId(msgdata.getToSessionId());
        messageDataAckBuilder.setSessionType(SessionType.SESSION_TYPE_SINGLE);

        IMHeader headerRes = header.clone();
        headerRes.setCommandId((short) MessageCmdID.CID_MSG_DATA_ACK_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageDataAckBuilder.build()));
        
        // 广播
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        if (routerConnector != null) {
            routerConnector.send(new IMProtoMessage<>(header, msgdata), true);
        }
        
        // 消息发送
        IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msgdata);

        ClientUser fromClientUser = ClientUserManager.getUserById(msgdata.getFromUserId());
        ClientUser toClientUser = ClientUserManager.getUserById(msgdata.getToSessionId());
        if (fromClientUser != null) {
            fromClientUser.broadcast(message, ctx);
        }
        if (toClientUser != null) {
            toClientUser.broadcast(message, null);
        }

        // 消息推送相关
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(msgdata.getToSessionId());
        
        // CID_OTHER_GET_DEVICE_TOKEN_REQ - CID_OTHER_GET_DEVICE_TOKEN_RES
        BaseModel<String> userTokenRes = iphonePushService.userDeviceToken(msgdata.getToSessionId());
        
        if (userTokenRes.getCode() == 0) {
            String userToken = userTokenRes.getData();
            if (userTokenRes != null) {
                List<UserTokenInfo> userTokenList = new ArrayList<>();
                UserTokenInfo tokenInfo = UserTokenInfo.newBuilder().setPushCount(1).setPushType(1)
                        .setToken(userToken)
                        .setUserId(msgdata.getToSessionId())
                        .setUserType(CommonUtils.getClientTypeOfToken(userToken)).build();
                userTokenList.add(tokenInfo);
                
                
                JSONObject groupPushJson = new JSONObject();
                groupPushJson.put("msg_type", msgdata.getMsgType().getNumber());
                groupPushJson.put("from_id", msgdata.getFromUserId());
                
                ClientUser imSenderUser = ClientUserManager.getUserById(msgdata.getFromUserId());
                String msgFlash = buildIosPushFlash(imSenderUser.getNickName(), msgdata);
                IMPushToUserReq pushToUserReq = IMPushToUserReq.newBuilder()
                        .setFlash(msgFlash).setData(groupPushJson.toJSONString())
                        .addAllUserTokenList(userTokenList).build();
                
                // 查询在线状态
                // 查询完成后，处理推送
                PduAttachData attachData = new PduAttachData(AttachType.PDU_FOR_PUSH,
                        0L, 0, pushToUserReq.toByteString());
                
                IMUsersStatReq userStatReq = IMUsersStatReq.newBuilder()
                        .setUserId(msgdata.getFromUserId()).addAllUserIdList(userIdList)
                        .setAttachData(ByteString.copyFrom(attachData.getBufferData())).build();
                
                IMHeader headerStatReq = new DefaultIMHeader(BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE);
                routerConnector.send(new IMProtoMessage<>(headerStatReq, userStatReq));
            }
        }

    }

    /**
     * 处理群消息发送
     * 
     * @param header
     * @param msgdata
     * @since  1.0
     */
    private void handleGroupMessage(IMHeader header, IMMsgData msgdata, ChannelHandlerContext ctx) {
        
        if (msgdata.getMsgId() == DBConstant.INVALIAD_VALUE) {
            logger.warn("Write group message to db failed, {}->{}.", msgdata.getFromUserId(), msgdata.getToSessionId());
            return ;
        }
        
        // 消息送达，接收反馈
        IMMessage.IMMsgDataAck.Builder messageDataAckBuilder = IMMessage.IMMsgDataAck.newBuilder();
        messageDataAckBuilder.setMsgId(msgdata.getMsgId());
        messageDataAckBuilder.setUserId(msgdata.getFromUserId());
        messageDataAckBuilder.setSessionId(msgdata.getToSessionId());
        messageDataAckBuilder.setSessionType(SessionType.SESSION_TYPE_GROUP);

        IMHeader headerRes = header.clone();
        headerRes.setCommandId((short) MessageCmdID.CID_MSG_DATA_ACK_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageDataAckBuilder.build()));
        
        // 广播
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        if (routerConnector != null) {
            routerConnector.send(new IMProtoMessage<>(header, msgdata), true);
        }
        
        // 服务器没有群的信息，向DB服务器请求群信息，并带上消息作为附件，返回时在发送该消息给其他群成员
        // 查询群员，然后推送消息
        BaseModel<GroupPushEntity> groupRes = groupService.getGroupPushInfo(msgdata.getToSessionId());
        if (groupRes.getCode() == GroupCmdResult.SUCCESS.getCode()) {
            if(groupRes.getData() != null ) {
                List<ShieldStatusEntity> memberShieldStatusList = groupRes.getData().getUserList();
                List<Long> userIdList = new ArrayList<>(); 
                List<UserTokenInfo> userTokenList = new ArrayList<>();
                
                // 群消息广播/推送
                IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msgdata);
                for (ShieldStatusEntity userShieldStatus : memberShieldStatusList) {
                    
                    if (userShieldStatus.getShieldStatus() == DBConstant.GROUP_STATUS_SHIELD) {
                        continue;
                    }
                    
                    // 处理token
                    if (userShieldStatus.getUserToken() != null) {
                        UserTokenInfo token = UserTokenInfo.newBuilder().setPushCount(1).setPushType(1)
                                .setToken(userShieldStatus.getUserToken())
                                .setUserId(userShieldStatus.getUserId())
                                .setUserType(CommonUtils.getClientTypeOfToken(userShieldStatus.getUserToken())).build();
                        userTokenList.add(token);
                    }
                    
                    
                    ClientUser clientUser = ClientUserManager.getUserById(userShieldStatus.getUserId());
                    if (clientUser == null) {
                        userIdList.add(userShieldStatus.getUserId());
                        
                        
                        continue;
                    }
                    if (userShieldStatus.getUserId() == msgdata.getFromUserId()) {
                        clientUser.broadcast(message, ctx);
                    } else {
                        clientUser.broadcast(message, null);
                        userIdList.add(userShieldStatus.getUserId());
                    }
                }
                
                // 群消息推送相关
                JSONObject groupPushJson = new JSONObject();
                groupPushJson.put("msg_type", msgdata.getMsgType().getNumber());
                groupPushJson.put("from_id", msgdata.getFromUserId());
                groupPushJson.put("group_id", msgdata.getToSessionId());
                
                ClientUser imSenderUser = ClientUserManager.getUserById(msgdata.getFromUserId());
                String msgFlash = buildIosPushFlash(imSenderUser.getNickName(), msgdata);
                IMPushToUserReq pushToUserReq = IMPushToUserReq.newBuilder()
                        .setFlash(msgFlash).setData(groupPushJson.toJSONString())
                        .addAllUserTokenList(userTokenList).build();
                
                // 查询在线状态
                // 查询完成后，处理推送
                PduAttachData attachData = new PduAttachData(AttachType.PDU_FOR_PUSH,
                        0L, 0, pushToUserReq.toByteString());
                
                IMUsersStatReq userStatReq = IMUsersStatReq.newBuilder()
                        .setUserId(msgdata.getFromUserId()).addAllUserIdList(userIdList)
                        .setAttachData(ByteString.copyFrom(attachData.getBufferData())).build();
                
                IMHeader headerStatReq = new DefaultIMHeader(BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE);
                routerConnector.send(new IMProtoMessage<>(headerStatReq, userStatReq));

//                // 推送
//                List<UserToken> userTokens = new ArrayList<>();
//                for (ShieldStatusEntity userShieldStatus: memberShieldStatusList) {
//                    if (msgdata.getFromUserId() == userShieldStatus.getUserId()) {
//                        continue;
//                    }
//                    
//                    ClientUser imUser = ClientUserManager.getUserById(userShieldStatus.getUserId());
//                    if (imUser != null) {
//                        UserToken userToken = new UserToken();
//                        
//                        // PC端登录后，不处理IOS推送
//                        userToken.setUserId(userShieldStatus.getUserId());
//                        userToken.setUserToken(userToken);
//                        userTokens.add(userToken);
//                    }
//                    
//                }
//                
//                // 推送当前Server的用户
//                
//                // 推送其他Server用户
            }
        }

    }

    /**
     * IOS推着内容
     * 
     * @param sender 发送者
     * @param msgdata 消息
     * @return IOS推送消息
     * @since  1.1
     */
    private String buildIosPushFlash(String sender, IMMsgData msgdata) {

        if (CommonUtils.isAudio(msgdata.getMsgType())) {
            if (sender == null) {
                return MessageConstant.COMMON_MSG;
            } else {
                StringBuffer sb = new StringBuffer();
                if (CommonUtils.isGroup(msgdata.getMsgType())) {

                    sb.append(sender).append("在群聊中发送了一条语音消息");
                } else {
                    sb.append(sender).append("给您发送了一条语音消息");
                }
                return sb.toString();
            }
            
        } else {
            if (sender == null) {
                return MessageConstant.COMMON_MSG;
            } else {
                byte[] conBytes =  SecurityUtils.getInstance().DecryptMsg(msgdata.getMsgData().toByteArray());
                String content = new String(conBytes); 
                
                // 判断开头与结尾
                if (content.startsWith(MessageConstant.IMAGE_MSG_START)
                        && content.endsWith(MessageConstant.IMAGE_MSG_END)) {
                    return MessageConstant.IMAGE_COMMON_MSG;
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append(sender).append("").append(content);
                    return sb.toString();
                }
            } 
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMMessageHandler#readMessage(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void readMessage(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // 消息已读回执
        IMMessage.IMMsgDataReadAck msgdata = (IMMessage.IMMsgDataReadAck) body;
        long userId = super.getUserId(ctx);

        ClearUserCountReq userCountReq = new ClearUserCountReq();
        userCountReq.setUserId(userId);
        userCountReq.setPeerId(msgdata.getSessionId());
        userCountReq.setSessionType(msgdata.getSessionType());
        messageService.clearUserCounter(userCountReq);

        IMMessage.IMMsgDataReadNotify.Builder messageReadNotifyBuilder = IMMessage.IMMsgDataReadNotify.newBuilder();
        messageReadNotifyBuilder.setMsgId(msgdata.getMsgId());
        messageReadNotifyBuilder.setUserId(msgdata.getUserId());
        messageReadNotifyBuilder.setSessionId(msgdata.getSessionId());
        messageReadNotifyBuilder.setSessionType(msgdata.getSessionType());

        IMHeader headerRes = header.clone();
        headerRes.setCommandId((short) MessageCmdID.CID_MSG_READ_NOTIFY_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageReadNotifyBuilder.build()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMMessageHandler#getUnreadCount(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void getUnreadCount(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMMessage.IMUnreadMsgCntReq unreadCountReq = (IMMessage.IMUnreadMsgCntReq) body;
        long userId = super.getUserId(ctx);

        try {

            // 未读信息
            BaseModel<List<UnreadEntity>> userMessage=messageService.getAllUnreadMsgCount(userId);
            
            List<IMBaseDefine.UnreadInfo> unreadInfos = new ArrayList<>();
            if (userMessage.getCode() == 0) {
                if (userMessage.getData() != null) {
                
                    for (UnreadEntity unreadEntity : userMessage.getData()) {
                        unreadInfos.add(JavaBean2ProtoBuf.getUnreadInfo(unreadEntity));
                    }                
                    
                }
            }

            IMMessage.IMUnreadMsgCntRsp.Builder unreadCountResBuilder = IMMessage.IMUnreadMsgCntRsp.newBuilder();
            unreadCountResBuilder.setTotalCnt(unreadInfos.size());
            unreadCountResBuilder.setUserId(userId);
            unreadCountResBuilder.addAllUnreadinfoList(unreadInfos);

            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_UNREAD_CNT_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, unreadCountResBuilder.build()));
        } catch (Exception e) {

            logger.error("未读消息处理异常", e);

            IMMessage.IMUnreadMsgCntRsp.Builder unreadCountResBuilder = IMMessage.IMUnreadMsgCntRsp.newBuilder();
            unreadCountResBuilder.setUserId(userId);
            unreadCountResBuilder.setTotalCnt(0);

            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_UNREAD_CNT_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, unreadCountResBuilder.buildPartial()));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMMessageHandler#getMessageList(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void getMessageList(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMMessage.IMGetMsgListReq messageListReq = (IMMessage.IMGetMsgListReq) body;
        long userId = super.getUserId(ctx);
        BaseModel<List<MessageEntity>> messageListRes = null;
        
        try {
            if (messageListReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_SINGLE) {
                // 获取个人消息
                messageListRes = messageService.getMessageList(userId, messageListReq.getSessionId(),
                        messageListReq.getMsgIdBegin(), messageListReq.getMsgCnt());
            } else if (messageListReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_GROUP) {
                // 获取群消息
                messageListRes = messageService.getGroupMessageList(userId, messageListReq.getSessionId(),
                        messageListReq.getMsgIdBegin(), messageListReq.getMsgCnt());
            } else {
                // 错误的类型
                logger.warn("错误的参数:SessionType=", messageListReq.getSessionType());
                return;
            }
            
            if (messageListRes!= null && messageListRes.getCode() == 0 && messageListRes.getData() != null) {

                List<IMBaseDefine.MsgInfo> messageList = new ArrayList<>();
                
                for (MessageEntity message: messageListRes.getData()) {
                    messageList.add(JavaBean2ProtoBuf.getMessageInfo(message));
                }
                
                IMMessage.IMGetMsgListRsp.Builder messageListResBuilder = IMMessage.IMGetMsgListRsp.newBuilder();
                messageListResBuilder.setUserId(userId);
                messageListResBuilder.setMsgIdBegin(messageListReq.getMsgIdBegin());
                messageListResBuilder.setSessionId(messageListReq.getSessionId());
                messageListResBuilder.setSessionType(messageListReq.getSessionType());
                
                messageListResBuilder.addAllMsgList(messageList);

                IMHeader headerRes = header.clone();
                headerRes.setCommandId((short) MessageCmdID.CID_MSG_LIST_RESPONSE_VALUE);

                // 返回消息列表
                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.build()));
            } else {
                IMMessage.IMGetMsgListRsp.Builder messageListResBuilder = IMMessage.IMGetMsgListRsp.newBuilder();
                messageListResBuilder.setUserId(userId);
                messageListResBuilder.setMsgIdBegin(messageListReq.getMsgIdBegin());
                messageListResBuilder.setSessionId(messageListReq.getSessionId());
                messageListResBuilder.setSessionType(messageListReq.getSessionType());
                
                IMHeader headerRes = header.clone();
                headerRes.setCommandId((short) MessageCmdID.CID_MSG_LIST_RESPONSE_VALUE);

                // 返回消息列表
                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.build()));
            }
            
        } catch (Exception e) {
            
            logger.error("消息列表处理异常", e);

            IMMessage.IMGetMsgListRsp.Builder messageListResBuilder = IMMessage.IMGetMsgListRsp.newBuilder();
            messageListResBuilder.setUserId(userId);
            messageListResBuilder.setMsgIdBegin(messageListReq.getMsgIdBegin());
            messageListResBuilder.setSessionId(messageListReq.getSessionId());
            messageListResBuilder.setSessionType(messageListReq.getSessionType());
            
            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_LIST_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.buildPartial()));
        }
        
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMMessageHandler#getLatestMessageId(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void getLatestMessageId(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMMessage.IMGetLatestMsgIdReq latestMsgIdReq = (IMMessage.IMGetLatestMsgIdReq) body;
        long userId = super.getUserId(ctx);

        try{
            BaseModel<Long> latestMsgIdRes = null;
            if (latestMsgIdReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_SINGLE) {
                // 获取个人消息
                latestMsgIdRes = messageService.getLatestMessageId(userId, latestMsgIdReq.getSessionId());
            } else if (latestMsgIdReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_GROUP) {
                // 获取群消息
                latestMsgIdRes = messageService.getLatestGroupMessageId(userId, latestMsgIdReq.getSessionId());
            } else {
                // 错误的类型
                logger.warn("错误的参数:SessionType=", latestMsgIdReq.getSessionType());
                return;
            }
            
            if (latestMsgIdRes!= null && latestMsgIdRes.getCode() == 0 && latestMsgIdRes.getData() != null) {
                IMMessage.IMGetLatestMsgIdRsp.Builder messageListResBuilder = IMMessage.IMGetLatestMsgIdRsp.newBuilder();
                messageListResBuilder.setLatestMsgId(latestMsgIdRes.getData());
                messageListResBuilder.setUserId(userId);
                messageListResBuilder.setSessionId(latestMsgIdReq.getSessionId());
                messageListResBuilder.setSessionType(latestMsgIdReq.getSessionType());
                
                IMHeader headerRes = header.clone();
                headerRes.setCommandId((short) MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_RSP_VALUE);

                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.build()));
            }
            
        } catch (Exception e) {
            
            logger.error("消息ID处理异常", e);

            IMMessage.IMGetLatestMsgIdRsp.Builder messageListResBuilder = IMMessage.IMGetLatestMsgIdRsp.newBuilder();
            // messageListResBuilder.setLatestMsgId(value);
            messageListResBuilder.setUserId(userId);
            messageListResBuilder.setSessionId(latestMsgIdReq.getSessionId());
            messageListResBuilder.setSessionType(latestMsgIdReq.getSessionType());
            
            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_RSP_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.buildPartial()));
        }
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMMessageHandler#getByMessageId(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void getByMessageId(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMMessage.IMGetMsgByIdReq getMsgByIdReq = (IMMessage.IMGetMsgByIdReq) body;
        long userId = super.getUserId(ctx);

        try{
            BaseModel<List<MessageEntity>> messageListRes = null;
            if (getMsgByIdReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_SINGLE) {
                // 获取个人消息
                messageListRes = messageService.getMessageById(userId, getMsgByIdReq.getSessionId(), getMsgByIdReq.getMsgIdListList());
            } else if (getMsgByIdReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_GROUP) {
                // 获取群消息
                messageListRes = messageService.getGroupMessageById(userId, getMsgByIdReq.getSessionId(), getMsgByIdReq.getMsgIdListList());
            } else {
                // 错误的类型
                logger.warn("错误的参数:SessionType=", getMsgByIdReq.getSessionType());
                return;
            }
            
            if (messageListRes != null && messageListRes.getCode() == 0 && messageListRes.getData() != null) {
                IMMessage.IMGetMsgByIdRsp.Builder messageListResBuilder = IMMessage.IMGetMsgByIdRsp.newBuilder();
                messageListResBuilder.setUserId(userId);
                messageListResBuilder.setSessionId(getMsgByIdReq.getSessionId());
                messageListResBuilder.setSessionType(getMsgByIdReq.getSessionType());
                
                List<IMBaseDefine.MsgInfo> messageInfoList = new ArrayList<>();
                for (MessageEntity message: messageListRes.getData()) {
                    messageInfoList.add(JavaBean2ProtoBuf.getMessageInfo(message));
                }
                messageListResBuilder.addAllMsgList(messageInfoList);
                
                IMHeader headerRes = header.clone();
                headerRes.setCommandId((short) MessageCmdID.CID_MSG_GET_BY_MSG_ID_RES_VALUE);
                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageListResBuilder.build()));
            }
            
        } catch (Exception e) {
            
            logger.error("消息ID处理异常", e);

            IMMessage.IMGetMsgByIdRsp.Builder messageResBuilder = IMMessage.IMGetMsgByIdRsp.newBuilder();
            messageResBuilder.setUserId(userId);
            messageResBuilder.setSessionId(getMsgByIdReq.getSessionId());
            messageResBuilder.setSessionType(getMsgByIdReq.getSessionType());
            
            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_GET_BY_MSG_ID_RES_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, messageResBuilder.buildPartial()));
        }
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMMessageHandler#clientMsgDataAck(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void clientMsgDataAck(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMMessage.IMMsgDataAck msgDataAck = (IMMessage.IMMsgDataAck) body;
        if (msgDataAck.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_SINGLE) {
            // TODO DelFromSendList(msg_id, session_id);
            logger.warn("+++ TODO +++");
            // ctx- conn > DelFromSendList
        }
    }

    @Override
    public void clientTimeReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        // IMMessage.IMClientTimeReq clientTimeReq = (IMMessage.IMClientTimeReq) body;

        IMMessage.IMClientTimeRsp clientTimeRsp = null;
        IMHeader headerRes = null;

        try {
            // 获取系统时间，单位为秒
            long time = CommonUtils.currentTimeSeconds();
            clientTimeRsp = IMMessage.IMClientTimeRsp.newBuilder().setServerTime((int) time).build();

            headerRes = header.clone();
            headerRes.setServiceId((short) ServiceID.SID_MSG_VALUE);
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_TIME_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, clientTimeRsp));

        } catch (Exception e) {

            logger.error("获取系统时间异常", e);
            headerRes = header.clone();
            headerRes.setServiceId((short) ServiceID.SID_MSG_VALUE);
            headerRes.setCommandId((short) MessageCmdID.CID_MSG_TIME_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, clientTimeRsp));
        }
    }
}
