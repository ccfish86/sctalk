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
public interface IMGroupHandler {

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void normalListReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void groupInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    
//    /**
//     * 
//     * @param header
//     * @param body
//     * @param ctx
//     * @since 1.0
//     */
//    void groupCreateReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    
//    
//    /**
//     * 
//     * @param header
//     * @param body
//     * @param ctx
//     * @since 1.0
//     */
//    void groupChangeMemberReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
//    
    
    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void createGroupReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    
    /**
     * 修改组成员
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void changeMemberReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);
    
    /**
     * 
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    void groupShieldReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
