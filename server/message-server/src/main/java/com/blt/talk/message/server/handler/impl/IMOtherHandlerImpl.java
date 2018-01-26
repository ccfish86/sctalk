/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.message.server.handler.IMOtherHandler;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMOtherHandlerImpl extends AbstractUserHandlerImpl implements IMOtherHandler {

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMOtherHandler#hearBeat(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void hearBeat(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // 响应
        ctx.writeAndFlush(new IMProtoMessage<>(header, body));
    }

    
	@Override
	public void StopReceivePacket(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
