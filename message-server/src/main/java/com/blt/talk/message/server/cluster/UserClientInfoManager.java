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

        /**
         * 删除连接
         * @param netId 连接id
         * @return 剩余连接数
         * @since  1.0
         */
        public int removeRouteConn(long netId) {
            this.netConnects.remove(netId);
            return this.netConnects.size();
        }

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
        public int removeClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.remove(clientType);
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
    }

}
