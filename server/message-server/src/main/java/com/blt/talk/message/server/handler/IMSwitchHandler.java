/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理p2p等
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMSwitchHandler {

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void switchP2p(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
