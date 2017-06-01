/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 通信录处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  3.0
 */
public interface IMBuddyListHandler {


    /**
     * 用户在线状态请求
     * <br>
     * 仅PC端
     * 
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void userStatusReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
