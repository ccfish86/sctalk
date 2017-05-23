package com.blt.talk.message.server.manager;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理客户端连接管理
 * <br>
 * 当前版本暂未处理同一用户登录多个设备的问题
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientConnectionMap {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    //保存一个gateway上所有的客户端连接
    public static ConcurrentHashMap<Long, ClientConnection> allClientMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, Long> userid2netidMap = new ConcurrentHashMap<>();

    public static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        Long netId = ctx.attr(ClientConnection.NETID).get();

        ClientConnection conn = allClientMap.get(netId);
        if(conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }
    
    public static ClientConnection getClientByUserId(Long userId) {
        Long netId = userid2netid(userId);
        if (netId != null) {
            return getClientConnection(netId);
        } else {
            return null;
        }
    }
    
    public static ClientConnection getClientConnection(long netId) {
        ClientConnection conn = allClientMap.get(netId);
        if(conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

    public static void addClientConnection(ChannelHandlerContext c) {
        //fixme 之后重复登录需要踢掉原来的连接
        ClientConnection conn = new ClientConnection(c);

        if(ClientConnectionMap.allClientMap.putIfAbsent(conn.getNetId(), conn) != null) {
            logger.error("Duplicated netid");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext c) {
        ClientConnection conn = getClientConnection(c);
        long netid = conn.getNetId();
        Long userid = conn.getUserId();
        if(userid != null && ClientConnectionMap.allClientMap.remove(netid) != null) {
            unRegisterUserid(userid);
        } else {
            logger.error("NetId: {} is not exist in allClientMap", netid);
        }

        logger.info("Client disconnected, netid: {}, userid: {}", netid, userid);
    }

    public static void registerUserid(Long userid, long netId) {
        if(userid2netidMap.putIfAbsent(userid, netId) == null) {
            ClientConnection conn = ClientConnectionMap.getClientConnection(netId);
            if(conn != null) {
                conn.setUserId(userid);
            } else {
                logger.error("ClientConnection is null");
                return;
            }
        } else {
            logger.error("userid: {} has registered in userid2netidMap", userid);
        }
    }

    protected static void unRegisterUserid(Long userid) {
        if(ClientConnectionMap.userid2netidMap.remove(userid) == null) {
            logger.error("UserId: {} is not exist in userid2netidMap", userid);
        }
    }

    public static Long userid2netid(Long userid) {
        Long netid = userid2netidMap.get(userid);
        if(netid != null)
            return netid;
        else {
            logger.error("User not login, userid: {}",userid);
        }
        return null;
    }
}
