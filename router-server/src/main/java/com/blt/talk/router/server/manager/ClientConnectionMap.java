package com.blt.talk.router.server.manager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 *  客户端连接
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientConnectionMap {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    // 保存一个gateway上所有的客户端连接
    private static ConcurrentHashMap<Long, ClientConnection> allClientMap = new ConcurrentHashMap<>();

    public static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        Long netId = ctx.attr(ClientConnection.NETID).get();

        ClientConnection conn = allClientMap.get(netId);
        if (conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

    public static ClientConnection getClientConnection(long netId) {
        ClientConnection conn = allClientMap.get(netId);
        if (conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

    public static void addClientConnection(ChannelHandlerContext c) {
        // fixme 之后重复登录需要踢掉原来的连接
        ClientConnection conn = new ClientConnection(c);

        if (ClientConnectionMap.allClientMap.putIfAbsent(conn.getNetId(), conn) != null) {
            logger.error("Duplicated netid");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext c) {
        ClientConnection conn = getClientConnection(c);
        long netid = conn.getNetId();
        logger.info("Client disconnected, netid: {}", netid);
    }

    public static Collection<ClientConnection> getAllClient() {
        return allClientMap.values();
    }
}
