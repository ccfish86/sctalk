/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMOtherHandler {

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void hearBeat(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void onlineUserInfo(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void onlineUserStatus(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void roleSet(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
