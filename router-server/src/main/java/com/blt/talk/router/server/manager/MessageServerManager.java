/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public final class MessageServerManager {
    
    private static Map<Long, MessageServerInfo> messageServerInfoMap = new HashMap<>();

    public static MessageServerInfo getServer(Long netId) {
        return messageServerInfoMap.get(netId);
    }

    public static void erase(Long netId) {
        if (messageServerInfoMap.containsKey(netId)) {
            messageServerInfoMap.remove(netId);
        }
    }
    
    public static void insert(Long netId, MessageServerInfo serverInfo) {
        messageServerInfoMap.put(netId, serverInfo);
    }

    public static void update(Long netId, int count) {
        if (messageServerInfoMap.containsKey(netId)) {
            messageServerInfoMap.get(netId).setUserCount(count);
        }
    }

    /**
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
    
    public static class  MessageServerInfo {
        
        private String ip;
        private Integer port;
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
