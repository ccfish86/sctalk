/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class MessageServerManager {

    private Map<String, MessageServerInfo> messageServerInfoMap;
    private String memberId;

    public MessageServerManager(HazelcastInstance hazelcastInstance) {
        this.messageServerInfoMap = hazelcastInstance.getMap("message-server#router#server");
        this.memberId = hazelcastInstance.getCluster().getLocalMember().getUuid();
    }

    /**
     * 通过连接ID查询对应的MessageServer信息
     * 
     * @return MessageServer信息
     * @since 1.0
     */
    public MessageServerInfo getServer() {
        return messageServerInfoMap.get(memberId);
    }

    /**
     * 根据【连接ID】移除MessageServer信息
     * 
     * @param clientUuid 客户端UUID
     * @since 1.0
     */
    public void unload(String clientUuid) {
        if (messageServerInfoMap.containsKey(clientUuid)) {
            messageServerInfoMap.remove(clientUuid);
        }
    }

    /**
     * 添加MessageServer信息
     * 
     * 
     * @param serverInfo MessageServer信息
     * @since 1.0
     */
    public void insert(MessageServerInfo serverInfo) {
        messageServerInfoMap.put(memberId, serverInfo);
    }

    /**
     * 更新MessageServer用户数
     * 
     * @param count 用户数（连接数）
     * @since 1.0
     */
    public void update(int count) {
        if (messageServerInfoMap.containsKey(memberId)) {
            messageServerInfoMap.get(memberId).setUserCount(count);
        }
    }

    /**
     * 获取可用的消息服务器 <br>
     * 遍历消息服务器列表，如果消息服务器的用户连接数不超过一定数(暂指定100)，直接返回当前消息服务器，否则取用户连接数最小的
     * 
     * @return
     * @since 1.0
     */
    public MessageServerInfo getUsableServer() {
        if (messageServerInfoMap.size() == 0) {
            return null;
        }

        MessageServerInfo minConnServer = null;

        for (MessageServerInfo server : messageServerInfoMap.values()) {

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
     * @since 1.0
     */
    public static class MessageServerInfo implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 3130423310364673999L;

        /** IP地址/主机地址 */
        private String ip;
        /** 端口号（Netty-Socket） */
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

    /**
     * @return
     * @since 1.0
     */
    public List<String> allServerNames() {
        if (messageServerInfoMap.size() == 0) {
            return null;
        }
        List<String> serverNames = new ArrayList<>();
        for (MessageServerInfo server : messageServerInfoMap.values()) {
            serverNames.add(server.getIp() + ":" + server.getPort());
        }
        return serverNames;
    }

}
