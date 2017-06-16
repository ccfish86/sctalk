/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import com.blt.talk.message.server.manager.ClientUser;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理用户连接通用类
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public abstract class AbstractUserHandlerImpl {

    /**
     * 取用户ID
     * @param ctx 用户连接
     * @return 用户ID
     * @since  1.0
     */
    protected long getUserId(ChannelHandlerContext ctx) {
        Long userId = ctx.attr(ClientUser.USER_ID).get();
        if (userId != null) {
            return userId;
        } 
        return 0;
    }
}
