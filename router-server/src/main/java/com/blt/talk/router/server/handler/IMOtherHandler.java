/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.handler;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * Other消息类型处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMOtherHandler {

    /**
     * 处理心跳包
     * <br>
     * 服务器端使用Netty，暂不需要特殊处理
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void hearBeat(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 在线用户信息更新
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void onlineUserInfo(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 在线用户状态更新
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void onlineUserStatus(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 权限设置
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void roleSet(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 更新消息服务器信息
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void updateMessageServer(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

    /**
     * 更新用户数
     * <br>
     * 由原login服务器移动过来，暂时与【在线用户信息更新重复】- 鸡肋
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    void updateUserCnt(IMHeader header, MessageLite body, ChannelHandlerContext ctx);

}
