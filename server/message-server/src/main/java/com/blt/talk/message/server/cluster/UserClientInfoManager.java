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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.util.CommonUtils;
import com.hazelcast.core.HazelcastInstance;

/**
 * 处理用户连接信息管理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public final class UserClientInfoManager implements InitializingBean {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /** 用户信息 */
    private Map<Long, UserClientInfo> userClientInfoMap;
    
    /** 连接ID与用户关系 */
    private Map<Long, Long> serverUserMap;
    
    /** 呼叫连接与用户关系 */
    private Map<Long, IMAVCall> hmCallerMap;
    /** 呼叫连接与被叫关系 */
    private Map<Long, IMAVCall> hmCalledMap;

    public Collection<UserClientInfo> allUsers() {
        return userClientInfoMap.values();
    }

    /**
     * 获取用户连接信息
     * 
     * @param userId 用户ID
     * @return
     * @since 1.0
     */
    public UserClientInfo getUserInfo(Long userId) {
        return userClientInfoMap.get(userId);
    }

    /**
     * 获取呼叫信息
     * 
     * @param fromId 用户ID
     * @since  1.3
     */
    public IMAVCall getCaller(Long fromId) {
        return hmCallerMap.get(fromId);
    }
    /**
     * 删除呼叫信息
     * 
     * @param userId 用户ID
     * @since  1.3
     */
    public void removeCaller(Long userId) {
        hmCallerMap.remove(userId);
    }
    
    /**
     * 设置呼叫信息
     * 
     * @param fromId 用户ID
     * @param netId 连接ID
     * @since  1.3
     */
    public void addCaller(Long fromId, Long netId) {
        IMAVCall avCall = new IMAVCall();
        avCall.setUserId(fromId);
        avCall.setNetId(netId);
        hmCallerMap.put(fromId, avCall);
    }
    /**
     * 获取被叫信息
     * 
     * @param toId 用户ID
     * @since  1.3
     */
    public IMAVCall getCalled(Long toId) {
        return hmCalledMap.get(toId);
    }

    /**
     * 删除被叫信息
     * 
     * @param userId 用户ID
     * @since  1.3
     */
    public void removeCalled(Long userId) {
        hmCalledMap.remove(userId);
    }

    /**
     * 设置被叫信息
     * 
     * @param toId 用户ID
     * @param netId 连接ID
     * @since  1.3
     */
    public void addCalled(Long toId, Long netId) {
        IMAVCall avCall = new IMAVCall();
        avCall.setUserId(toId);
        avCall.setNetId(netId);
        hmCalledMap.put(toId, avCall);
    }
    /**
     * 取服务器的用户
     * @param netIds 连接ID列表（依赖hazelcast）
     * @return
     * @since  1.0
     */
    public Collection<Long> getUserList(Collection<Long> netIds) {
        Collection<Long> userList = new ArrayList<>();
        for (Long netId : netIds) {
            Long userId = serverUserMap.get(netId);
            if (userId != null) {
                userList.add(userId);
            }
        }
        return userList;
    }
    /**
     * 删除用户连接信息 <br>
     * 用户所有端离线时
     * 
     * @param userId 用户ID
     * @since 1.0
     */
    public void erase(Long userId, Long netid) {
        if (userClientInfoMap.containsKey(userId)) {
            userClientInfoMap.remove(userId);
        }
        serverUserMap.remove(netid, userId);
    }

    /**
     * 删除用户连接信息 <br>
     * 服务器离线时
     * 
     * @param netIds 连接ID列表
     * @since 1.0
     */
    public void unloadServer(Collection<Long> netIds) {
        Collection<Long> userList = new ArrayList<>();
        for (Long netId : netIds) {
            Long userId = serverUserMap.remove(netId);
            if (userId != null) {
                userList.add(userId);
            }
        }
        if (!userList.isEmpty()) {
            for (Long userId : userList) {
                UserClientInfo userClientInfo = userClientInfoMap.get(userId);
                userClientInfo.netConnects.removeAll(netIds);
                if (userClientInfo.netConnects.isEmpty()) {
                    // erase(userId);
                    userClientInfoMap.remove(userId);
                } else {
                    userClientInfoMap.put(userId, userClientInfo);
                }
            }
        }
    }
    
    /**
     * 添加用户连接信息
     * 
     * @param userId 用户ID
     * @param userClientInfo 用户连接信息
     * @since 1.0
     */
    public void insert(Long userId, UserClientInfo userClientInfo) {
        userClientInfoMap.put(userId, userClientInfo);
        List<Long> routeConns = userClientInfo.getRouteConns();
        if (routeConns != null) {
            for (Long conn : routeConns) {
                serverUserMap.put(conn, userId);
            }
        }
    }
    /**
     * 更新用户连接信息
     * 
     * @param userId 用户ID
     * @param userClientInfo 用户连接信息
     * @since 1.0
     */
    public void update(Long userId, UserClientInfo userClientInfo) {
        if (userClientInfoMap.containsKey(userId)) {
            userClientInfoMap.put(userId, userClientInfo);
            List<Long> routeConns = userClientInfo.getRouteConns();
            if (routeConns != null) {
                for (Long conn : routeConns) {
                    serverUserMap.put(conn, userId);
                }
            }
        }
    }

    /**
     * 用户呼叫信息
     * 
     * @author 袁贵
     * @version 1.0
     * @since  1.0
     */
    public static class IMAVCall implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1645682202612564754L;
        
        private Long userId;

        private Long netId;
        
        private Long callId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getNetId() {
            return netId;
        }

        public void setNetId(Long netId) {
            this.netId = netId;
        }

        public Long getCallId() {
            return callId;
        }

        public void setCallId(Long callId) {
            this.callId = callId;
        }
    }
    
    /**
     * 用户连接信息
     * 
     * @author 袁贵
     * @version 1.0
     * @since 1.0
     */
    public static class UserClientInfo implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 4078865102805121742L;

        private Long userId;
//        
//        /** 服务器ID */
//        private List<String> uuids;

        /**
         * @return the userId
         */
        public Long getUserId() {
            return userId;
        }

        /**
         * @param userId the userId to set
         */
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        private List<Long> netConnects = new ArrayList<>();
        private Set<IMBaseDefine.ClientType> clientTypes = new HashSet<>();

        public void addRouteConn(long netId) {
            this.netConnects.add(netId);
        }

//        public String getUuid() {
//            return uuid;
//        }
//
//        public void setUuid(String uuid) {
//            this.uuid = uuid;
//        }

        public boolean findRouteConn(Long netId) {
            return this.netConnects.contains(netId);
        }

        public boolean isMsgConnNULL() {
            return clientTypes.isEmpty();
        }

        /**
         * 获取用户的连接
         * @return
         * @since  1.0
         */
        public List<Long> getRouteConns() {
            return netConnects;
        }

        /**
         * 追加用户客户端类型
         * 
         * @param clientType 客户端类型
         * @since 1.0
         */
        public void addClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.add(clientType);
        }

        /**
         * 删除用户客户端类型
         * 
         * @param clientType 客户端类型
         * @return 客户端类型数
         * @since 1.0
         */
        public int removeClient(IMBaseDefine.ClientType clientType, Long netId) {
            this.clientTypes.remove(clientType);
            this.netConnects.remove(netId);
            return clientTypes.size();
        }

        /**
         * 判断用户的在线状态 <br>
         * 这里仅限PC端，移动端由于网络问题，暂不做在线/离线状态依据
         * 
         * @return 用户是否在线
         * @since 1.0
         */
        public UserStatType getStatus() {
            IMBaseDefine.UserStatType status = IMBaseDefine.UserStatType.USER_STATUS_OFFLINE;
            for (IMBaseDefine.ClientType clientType : clientTypes) {
                if (CommonUtils.isPc(clientType)) {
                    status = IMBaseDefine.UserStatType.USER_STATUS_ONLINE;
                    break;
                }
            }
            return status;
        }

        /**
         * 判断用户的PC端是否登录
         * 
         * @return PC端是否登录
         * @since 1.0
         */
        public boolean isPCClientLogin() {

            for (IMBaseDefine.ClientType clientType : clientTypes) {
                if (CommonUtils.isPc(clientType)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.userClientInfoMap = hazelcastInstance.getMap("message-server#router#user");
        this.serverUserMap = hazelcastInstance.getMap("message-server#router#connection");
        this.hmCallerMap = hazelcastInstance.getMap("message-server#router#caller");
        this.hmCalledMap = hazelcastInstance.getMap("message-server#router#called");
    }


}
