/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.manager;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 客户端连接的封装类
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class ClientConnection {

    private static final AtomicLong netidGenerator = new AtomicLong(0);

    ClientConnection(ChannelHandlerContext c) {
        _netId = netidGenerator.incrementAndGet();
        _ctx = c;
        _ctx.attr(ClientConnection.NETID).set(_netId);
    }

    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");

    private long _netId;
    private ChannelHandlerContext _ctx;

    private boolean _master;

    public long getNetId() {
        return _netId;
    }

    public boolean isMaster() {
        return _master;
    }

    public void setMaster(boolean master) {
        this._master = master;
    }

    public void readUserIdFromDB() {

    }

    public void setCtx(ChannelHandlerContext ctx) {
        _ctx = ctx;
    }

    public ChannelHandlerContext getCtx() {
        return _ctx;
    }
}
