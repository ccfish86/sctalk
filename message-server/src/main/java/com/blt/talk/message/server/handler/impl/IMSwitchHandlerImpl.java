/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMMessage;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.message.server.handler.IMSwitchHandler;
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
public class IMSwitchHandlerImpl implements IMSwitchHandler {

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMSwitchHandler#switchP2p(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void switchP2p(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMSwitchService.IMP2PCmdMsg p2pCmdMsg = (IMSwitchService.IMP2PCmdMsg) body;
        long toId = p2pCmdMsg.getToUserId();
        
        // FIXME 需要确认一下逻辑
//        ClientConnection clientConn = ClientConnectionMap.getClientByUserId(String.valueOf(toId));
//        if (clientConn != null) {
//            clientConn.getCtx().writeAndFlush(new IMProtoMessage<IMSwitchService.IMP2PCmdMsg>(header.clone(), p2pCmdMsg));
//        }
    }

}
