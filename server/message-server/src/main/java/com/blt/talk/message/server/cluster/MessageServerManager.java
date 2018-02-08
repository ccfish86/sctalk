/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class MessageServerManager {

    private Map<String, MessageServerInfo> messageServerInfoMap;
    private MultiMap<String, Long> messageServerConnectionMap;
    private String memberId;
    
    @Autowired
    private UserClientInfoManager userClientInfoManager;

    public MessageServerManager(HazelcastInstance hazelcastInstance) {
        this.messageServerInfoMap = hazelcastInstance.getMap("message-server#router#server");
        this.messageServerConnectionMap = hazelcastInstance.getMultiMap("message-server#router#server#connection");
        this.memberId = hazelcastInstance.getCluster().getLocalMember().getUuid();
    }

    public void addConnect(String uuid, Long netId) {
        messageServerConnectionMap.put(uuid, netId);
    }

    public MessageServerInfo getServerBy(Long netId) {
        for (String uuid: messageServerConnectionMap.keySet()) {
            if (messageServerConnectionMap.containsEntry(uuid, netId)) {
                return messageServerInfoMap.get(uuid);
            }
        }
        return null;
    }
    
    public String getMemberByNetId(Long netId) {
        String targetUuid = null;
        for (String uuid: messageServerConnectionMap.keySet()) {
            if (messageServerConnectionMap.containsEntry(uuid, netId)) {
                targetUuid = uuid;
                break;
            }
        }

        return targetUuid;
    }
    public Set<String> getMemberByNetIds(List<Long> netIds) {
        Set<String> targetUuids = new HashSet<>();
        for (String uuid: messageServerConnectionMap.keySet()) {
            for (Long netId: netIds) {
                if (messageServerConnectionMap.containsEntry(uuid, netId)) {
                    targetUuids.add(uuid);
                }
            }
        }

        return targetUuids;
    }
    
    public void removeConnect(String uuid, Long netId) {
        messageServerConnectionMap.remove(uuid, netId);
    }
    public int getConnectCount(String uuid) {
        return messageServerConnectionMap.valueCount(uuid);
    }

    public Collection<Long> getUserList(String uuid) {
        Collection<Long> netIds = messageServerConnectionMap.get(uuid);
        return userClientInfoManager.getUserList(netIds);
    }
    
    /**
     * 通过连接ID查询对应的MessageServer信息
     * 
     * @return MessageServer信息
     * @since 1.0
     */
    public MessageServerInfo getServer(String uuid) {
        return messageServerInfoMap.get(uuid);
    }

    /**
     * 根据【连接ID】移除MessageServer信息
     * 
     * @param clientUuid 客户端UUID
     * @since 1.0
     */
    public void unload(String clientUuid) {
        messageServerInfoMap.remove(clientUuid);
        Collection<Long> netIds = messageServerConnectionMap.remove(clientUuid);
        userClientInfoManager.unloadServer(netIds);
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
        /** 主IP地址（外网IP） */
        private String priorIP;
        /** 端口号（Netty-Socket） */
        private Integer port;

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
         * @return the priorIP
         */
        public String getPriorIP() {
            return priorIP;
        }

        /**
         * @param priorIP the priorIP to set
         */
        public void setPriorIP(String priorIP) {
            this.priorIP = priorIP;
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

    }

    /**
     * @return
     * @since 1.0
     */
    public List<String> allServerIds() {
        if (messageServerInfoMap.size() == 0) {
            return null;
        }
        List<String> serverIds = new ArrayList<>();
        for (String uuid : messageServerInfoMap.keySet()) {
            serverIds.add(uuid);
        }
        return serverIds;
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
