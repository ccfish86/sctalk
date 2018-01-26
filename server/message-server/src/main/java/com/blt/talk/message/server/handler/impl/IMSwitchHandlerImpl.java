/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.handler.IMSwitchHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
//import com.blt.talk.message.server.manager.ClientConnection;
//import com.blt.talk.message.server.manager.ClientConnectionMap;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMSwitchHandlerImpl extends AbstractUserHandlerImpl implements IMSwitchHandler {

//    @Autowired
//    private RouterHandler routerHandler;
    @Autowired
    private MessageServerCluster messageServerCluster;
    
    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMSwitchHandler#switchP2p(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void switchP2p(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
//        IMSwitchService.IMP2PCmdMsg p2pCmdMsg = (IMSwitchService.IMP2PCmdMsg) body;
//        long toId = p2pCmdMsg.getToUserId();
        
        // FIXME 需要确认一下逻辑
//        ClientConnection clientConn = ClientConnectionMap.getClientByUserId(String.valueOf(toId));
//        if (clientConn != null) {
//            clientConn.getCtx().writeAndFlush(new IMProtoMessage<IMSwitchService.IMP2PCmdMsg>(header.clone(), p2pCmdMsg));
//        }
        IMSwitchService.IMP2PCmdMsg p2pCmdMsg = (IMSwitchService.IMP2PCmdMsg)body;
        // 设置用户的ID
        long userId = super.getUserId(ctx);
        p2pCmdMsg = p2pCmdMsg.toBuilder().setFromUserId(userId).build();

        IMProtoMessage<MessageLite>  swithP2pMsg = new IMProtoMessage<MessageLite>(header, body);
    	
    	long toId = p2pCmdMsg.getToUserId();
        long fromId =  p2pCmdMsg.getFromUserId();

    	ClientUser toClientUser = ClientUserManager.getUserById(toId);
    	
        ClientUser fromClientUser = ClientUserManager.getUserById(fromId);
               
        //处理是否正确需要确认？
        if (toClientUser != null ){
        	toClientUser.broadcast(swithP2pMsg, ctx);
        }
        
        if (fromClientUser != null) {
        	fromClientUser.broadcast(swithP2pMsg, null);
        }
        
        ClientUserManager.broadCast(swithP2pMsg, SysConstant.CLIENT_TYPE_FLAG_BOTH);
        
        // 通过路由进行转发
        // routerHandler.send(header, body);
        messageServerCluster.send(header, body);
    }

}
