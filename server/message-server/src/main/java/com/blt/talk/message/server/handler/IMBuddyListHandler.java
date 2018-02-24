/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  3.0
 */
public interface IMBuddyListHandler {

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  3.0
     */
    void recentContactReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void userInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void removeSessionReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void allUserReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void userStatusReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void changeAvaterReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void departmentReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.0
     */
    void changeSignInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 修改用户信息
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接context
     * @since  1.1
     */
    void changeUserInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
}
