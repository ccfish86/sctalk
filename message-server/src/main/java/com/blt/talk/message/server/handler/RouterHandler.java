/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.proto.IMBaseDefine.KickReasonType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMServer.IMUserStatusUpdate;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface RouterHandler {

    /**
     * 
     * @param msgCtx 用户的连接
     * @param userStatusOnline 状态
     * @since  1.0
     */
    void sendUserStatusUpdate(ChannelHandlerContext msgCtx, UserStatType userStatusOnline);

    /**
     * @param msgCtx
     * @param kickReasonDuplicateUser
     * @since  1.0
     */
    void kickOutSameClientType(ChannelHandlerContext msgCtx, KickReasonType kickReasonDuplicateUser);
    
    /**
     * @param header
     * @param messageLite
     * @since  1.0
     */
    void send(IMHeader header, MessageLite messageLite);
    
    /**
     * @param header
     * @param messageLite
     * @param retry 是否支持重发
     * @since  1.0
     */
    void send(IMHeader header, MessageLite messageLite, boolean retry);
}
