/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.util.CommonUtils;

/**
 * 处理用户连接信息管理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public final class UserClientInfoManager {

    private static Map<Long, UserClientInfo> userClientInfoMap = new HashMap<>();
    
    public static void updateInfo() {
        
    }
    
    /**
     * 获取用户连接信息
     * 
     * @param userId 用户ID
     * @return
     * @since  1.0
     */
    public static UserClientInfo getUserInfo(Long userId) {
        return userClientInfoMap.get(userId);
    }

    /**
     * 删除用户连接信息
     * <br>
     * 用户所有端离线时
     * 
     * @param userId 用户ID
     * @since  1.0
     */
    public static void erase(Long userId) {
        if (userClientInfoMap.containsKey(userId)) {
            userClientInfoMap.remove(userId);
        }
    }

    /**
     * 添加用户连接信息
     * 
     * @param userId 用户ID
     * @param userClientInfo 用户连接信息
     * @since  1.0
     */
    public static void insert(Long userId, UserClientInfo userClientInfo) {
        userClientInfoMap.put(userId, userClientInfo);
    }
    
    /**
     * 用户连接信息
     * 
     * @author 袁贵
     * @version 1.0
     * @since  1.0
     */
    public static class UserClientInfo {
        
        private List<Long> netConnects = new ArrayList<>();
        private Set<IMBaseDefine.ClientType> clientTypes = new HashSet<>();
        
        public void addRouteConn(Long netId) {
            this.netConnects.add(netId);
        }
        public void removeRouteConn(Long netId) {
            this.netConnects.remove(netId);
        }

        public boolean findRouteConn(Long netId) {
            return this.netConnects.contains(netId);
        }
        public boolean isMsgConnNULL() {
            return clientTypes.isEmpty();
        }
        
        /**
         * 获取连接数（MessageServer-RouterServer）
         * 
         * @return 连接数
         * @since  1.0
         */
        public int getRouteConnCount() {
            return netConnects.size();
        }
        
        /**
         * 追加用户客户端类型
         * 
         * @param clientType 客户端类型
         * @since  1.0
         */
        public void addClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.add(clientType);
        }

        /**
         * 删除用户客户端类型
         * 
         * @param clientType 客户端类型
         * @since  1.0
         */
        public void removeClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.remove(clientType);
        }
        /**
         * 判断用户的在线状态
         * <br>
         * 这里仅限PC端，移动端由于网络问题，暂不做在线/离线状态依据
         * 
         * @return 用户是否在线
         * @since  1.0
         */
        public UserStatType getStatus() {
            IMBaseDefine.UserStatType status = IMBaseDefine.UserStatType.USER_STATUS_OFFLINE;
            for (IMBaseDefine.ClientType clientType: clientTypes) {
                if (CommonUtils.isPc(clientType)) {
                    status = IMBaseDefine.UserStatType.USER_STATUS_ONLINE;
                    break;
                }
            }
            return status;
        }
        /**
         * 判断用户的PC端是否登录
         * @return PC端是否登录
         * @since  1.0
         */
        public boolean isPCClientLogin() {

            for (IMBaseDefine.ClientType clientType: clientTypes) {
                if (CommonUtils.isPc(clientType)) {
                    return true;
                }
            }
            return false;
        }
    }
    
}
