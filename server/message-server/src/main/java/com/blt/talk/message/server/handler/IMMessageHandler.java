/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理Handler
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMMessageHandler {

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void sendMessage(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void readMessage(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void getUnreadCount(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void getMessageList(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void getLatestMessageId(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void getByMessageId(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void clientMsgDataAck(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    
    
    /**
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since 1.0
     */
	void clientTimeReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
