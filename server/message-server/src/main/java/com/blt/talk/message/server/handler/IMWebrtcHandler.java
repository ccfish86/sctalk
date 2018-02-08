/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理webrtc消息
 * 
 * @author 袁贵
 * @version 1.3
 * @since  1.3
 */
public interface IMWebrtcHandler {

    /**
     * 初始化会话
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 信道
     * @since  1.3
     */
    void initiateReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 初始化会话响应
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 信道
     * @since  1.3
     */
    void initiateRes(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 挂断
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 信道
     * @since  1.3
     */
    void hungupCall(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
