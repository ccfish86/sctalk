/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理消息服务器管理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public final class MessageServerManager {
    
    private static Map<Long, MessageServerInfo> messageServerInfoMap = new HashMap<>();

    /**
     * 通过连接ID查询对应的MessageServer信息
     * 
     * @param netId 连接ID
     * @return MessageServer信息
     * @since  1.0
     */
    public static MessageServerInfo getServer(Long netId) {
        return messageServerInfoMap.get(netId);
    }

    /**
     * 根据【连接ID】移除MessageServer信息
     * 
     * @param netId 连接ID
     * @since  1.0
     */
    public static void erase(Long netId) {
        if (messageServerInfoMap.containsKey(netId)) {
            messageServerInfoMap.remove(netId);
        }
    }
    
    /**
     * 添加MessageServer信息
     * @param netId 连接ID
     * @param serverInfo MessageServer信息
     * @since  1.0
     */
    public static void insert(Long netId, MessageServerInfo serverInfo) {
        messageServerInfoMap.put(netId, serverInfo);
    }

    /**
     * 更新MessageServer用户数
     * 
     * @param netId 连接ID
     * @param count 用户数（连接数）
     * @since  1.0
     */
    public static void update(Long netId, int count) {
        if (messageServerInfoMap.containsKey(netId)) {
            messageServerInfoMap.get(netId).setUserCount(count);
        }
    }

    /**
     * 获取可用的消息服务器
     * <br>
     * 遍历消息服务器列表，如果消息服务器的用户连接数不超过一定数(暂指定100)，直接返回当前消息服务器，否则取用户连接数最小的
     * 
     * @return
     * @since  1.0
     */
    public static MessageServerInfo getUsableServer() {
        System.out.println(messageServerInfoMap);
        if (messageServerInfoMap.size() == 0) {
            return null;
        }
        
        MessageServerInfo minConnServer = null;
        
        for (MessageServerInfo server: messageServerInfoMap.values()) {
            
            // 如果当前服务器连接数不到100， 直接返回
            if (server.getUserCount() < 100) {
                return server;
            } else {
                if (minConnServer == null) {
                    minConnServer = server;
                } else if (minConnServer.getUserCount() > server.getUserCount()) {
                    minConnServer = server;
                } else {
                    // nth to do 
                }
            }
        }
        
        return minConnServer;
    }
    
    /**
     * 消息服务器信息
     * 
     * @author 袁贵
     * @version 1.0
     * @since  1.0
     */
    public static class  MessageServerInfo {
        
        /** IP地址/主机地址 */
        private String ip;
        /** 端口号（Netty-Socket）  */
        private Integer port;
        /** 用户数 */
        private int userCount;
        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }
        /**
         * @param ip the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }
        /**
         * @return the port
         */
        public Integer getPort() {
            return port;
        }
        /**
         * @param port the port to set
         */
        public void setPort(Integer port) {
            this.port = port;
        }
        /**
         * @return the userCount
         */
        public int getUserCount() {
            return userCount;
        }
        /**
         * @param userCount the userCount to set
         */
        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }
        
    }
}
