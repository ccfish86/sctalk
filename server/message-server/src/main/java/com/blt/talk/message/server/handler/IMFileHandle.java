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
 * @since  1.0
 */
public interface IMFileHandle {

    void fileReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    void hasOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    void addOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    void delOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
}
