/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine.AVCallCmdId;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMWebRTC.IMAVCallCancelReq;
import com.blt.talk.common.code.proto.IMWebRTC.IMAVCallHungUpReq;
import com.blt.talk.common.code.proto.IMWebRTC.IMAVCallInitiateReq;
import com.blt.talk.common.code.proto.IMWebRTC.IMAVCallInitiateRes;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.handler.IMWebrtcHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.3
 * @since  1.3
 */
@Component
public class IMWebrtcHandlerImpl extends AbstractUserHandlerImpl implements IMWebrtcHandler {

    @Autowired
    private MessageServerCluster messageServerCluster;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMWebrtcHandler#initiateReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void initiateReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMAVCallInitiateReq msg = (IMAVCallInitiateReq)body;
        long fromId = msg.getFromId();
        long toId = msg.getToId();
//        // long callId = msg.getCallId();
//        // FIXME: ?sdp msg.getAttachData()
//        
//        // IMBaseDefine.ClientType nType = msg.getCallerClientType();
//        // logger.debug("webrtc initiate request {} {} {} {}", fromId, toId, callId, nType);
//
//        ClientUser toClientUser = ClientUserManager.getUserById(toId);
//        if (toClientUser != null ){
//            IMHeader hdRequest = header.clone();
//            hdRequest.setSeqnum(0);
//            IMProtoMessage<MessageLite>  msgCancel = new IMProtoMessage<MessageLite>(hdRequest, body);
//            toClientUser.broadcast(msgCancel, ctx);
//        }
// 
//        messageServerCluster.send(header, body);
        ListenableFuture<?> future = messageServerCluster.webrtcInitateCallReq(fromId, toId, super.getHandleId(ctx));
        future.addCallback((result) -> {
            messageServerCluster.sendToUser(toId, header, body);
        }, (throwable) -> {
            // TODO 发起失败
        });
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMWebrtcHandler#initiateRes(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void initiateRes(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMAVCallInitiateRes msg = (IMAVCallInitiateRes)body;
        long fromId = msg.getFromId();
        long toId = msg.getToId();
        long callId = msg.getCallId();

        ListenableFuture<?> future =messageServerCluster.webrtcInitateCallRes(fromId, toId, super.getHandleId(ctx));
        
        future.addCallback((result) -> {
            // TODO 通知其他端取消
            // messageServerCluster.sendToUser(toId, header, body);
        }, (throwable) -> {
            // TODO 接受失败
            // 所有端取消
        });
        // IMBaseDefine.ClientType nType = msg.getCalledClientType();
        // logger.debug("webrtc initiate resposne {} {} {} {}", fromId, toId, callId, nType);
        
//        ClientUser toClientUser = ClientUserManager.getUserById(toId);
//        if (toClientUser != null ){
//            
//            // 呼叫接起时，取消其他端的呼叫提醒
//            IMHeader hdCancel = new IMHeader();
//            hdCancel.setServiceId(ServiceID.SID_AVCALL_VALUE);
//            hdCancel.setCommandId(AVCallCmdId.CID_AVCALL_CANCEL_REQ_VALUE);
//            
//            IMAVCallCancelReq msgCancelReq = IMAVCallCancelReq.newBuilder().setFromId(fromId)
//                    .setToId(toId).setCallId(callId).build();
//            
//            IMProtoMessage<MessageLite>  msgCancel = new IMProtoMessage<MessageLite>(hdCancel, msgCancelReq);
//            toClientUser.broadcast(msgCancel, ctx);
//        }
//        
//        messageServerCluster.send(header, body);
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMWebrtcHandler#hungupCall(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void hungupCall(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMAVCallHungUpReq msg = (IMAVCallHungUpReq)body;
        long fromId = msg.getFromId();
        long toId = msg.getToId();
        // messageServerCluster.send(header, body);
        // TODO 挂断  处理
        messageServerCluster.webrtcHungupReq(fromId, toId, super.getHandleId(ctx));
    }

}
