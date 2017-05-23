package com.blt.talk.message.server.manager;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import thirdparty.threedes.ThreeDES;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 客户端连接信息的封装类
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientConnection {
    private static final AtomicLong netidGenerator = new AtomicLong(0);
    ClientConnection(ChannelHandlerContext c) {
        _netId = netidGenerator.incrementAndGet();
        _ctx = c;
        _ctx.attr(ClientConnection.NETID).set(_netId);
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    // public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");
    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");
    public static AttributeKey<Long> USERID = AttributeKey.valueOf("userId");

    private Long _userId;
    private long _netId;
    private ChannelHandlerContext _ctx;

    public long getNetId() {
        return _netId;
    }

    public Long getUserId() {
        return _userId;
    }

    public void setUserId(Long userId) {
        _userId = userId;
        _ctx.attr(ClientConnection.USERID).set(userId);
    }

    public void readUserIdFromDB() {

    }

    public void setCtx(ChannelHandlerContext ctx) {_ctx = ctx;}
    public ChannelHandlerContext getCtx() {
        return _ctx;
    }
}
