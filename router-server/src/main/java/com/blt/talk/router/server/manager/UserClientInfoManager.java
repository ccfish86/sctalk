/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
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
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public final class UserClientInfoManager {

    private static Map<Long, UserClientInfo> userClientInfoMap = new HashMap<>();
    
    public static void updateInfo() {
        
    }
    
    public static UserClientInfo getUserInfo(Long userId) {
        return userClientInfoMap.get(userId);
    }

    public static void erase(Long userId) {
        if (userClientInfoMap.containsKey(userId)) {
            userClientInfoMap.remove(userId);
        }
    }

    public static void insert(Long userId, UserClientInfo userClientInfo) {
        userClientInfoMap.put(userId, userClientInfo);
    }
    
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
        
        public int getRouteConnCount() {
            return netConnects.size();
        }
        
        public void addClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.add(clientType);
        }

        public void removeClientType(IMBaseDefine.ClientType clientType) {
            this.clientTypes.remove(clientType);
        }
        
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
         * @return
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
