/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 登录相关Handler
 * @author 袁贵
 * @version 1.0
 * @since  3.0
 */
public interface IMLoginHandler {
    
    /**
     * 
     * @param header 消息头
     * @param msg
     * @param ctx 连接context
     * @throws Exception
     * @since  3.0
     */
    public void login(IMHeader header, MessageLite msg, ChannelHandlerContext ctx) throws Exception;

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void logOut(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void kickUser(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void deviceToken(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void kickPcClient(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void pushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    public void queryPushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
}
