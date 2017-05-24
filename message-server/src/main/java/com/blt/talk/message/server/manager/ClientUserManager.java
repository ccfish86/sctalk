/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.constant.SysConstant;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端用户管理 
 * <br>
 * 管理用户多端的连接
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientUserManager {

    private static Map<Long, ClientUser> userMap = new HashMap<>();
//    private static Map<String, ClientUser> userNameMap = new HashMap<>();
    
    public static ClientUser getUserById(Long userId) {
        return userMap.get(userId);
    }
    
//    public static ClientUser getUserByName(String userName) {
//        return userNameMap.get(userName);
//    }
    
    public static ChannelHandlerContext getConnByHandle(Long userId, int handle) {
        ClientUser client = userMap.get(userId);
        if (client == null) {
            return null;
        }
        return client.getConn(handle);
    }
    
//    public static boolean addUserByName(String userName, ClientUser clientUser) {
//        if (!userNameMap.containsKey(userName)) {
//            userNameMap.put(userName, clientUser);
//            return true;
//        }
//        return false;
//    }
//    public static void removeUserByName(String userName) {
//        if (userNameMap.containsKey(userName)) {
//            userNameMap.remove(userName);
//        }
//    }
    
    public static boolean addUserById(Long userId, ClientUser clientUser) {
        if (!userMap.containsKey(userId)) {
            userMap.put(userId, clientUser);
            return true;
        }
        return false;
    }
    public static void removeUserById(Long userId) {
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
        }
    }
    
    public static void removeUser(ClientUser clientUser) {
        removeUserById(clientUser.getUserId());
//        removeUserByName(clientUser.getLoginName());
    }
    
    public static void removeAll() {
        userMap.clear();
//        userNameMap.clear();
    }
    
    /**
     * 获取用户连接状态
     * 
     * @return
     * @since  1.0
     */
    public static List<IMBaseDefine.ServerUserStat> getOnlineUser() {
        List<IMBaseDefine.ServerUserStat> userStats = new ArrayList<>();
        for (ClientUser user : userMap.values()) {
            if (user.isValidate()) {
                Map<Long, ChannelHandlerContext> connMap = user.getConnMap();
                for (ChannelHandlerContext cxt: connMap.values()) {
                    
                    if (cxt.channel().isOpen()) {
                        IMBaseDefine.ServerUserStat.Builder userStatBuilder = IMBaseDefine.ServerUserStat.newBuilder();
                        userStatBuilder.setUserId(user.getUserId());
                        userStatBuilder.setClientType(cxt.attr(ClientUser.CLIENT_TYPE).get());
                        userStatBuilder.setStatus(cxt.attr(ClientUser.STATUS).get());
                        
                        userStats.add(userStatBuilder.build());
                    }
                }
            }
        }
        return userStats;
    }
    public static ClientUserConn getUserConn() {
        ClientUserConn clientUserConn = new ClientUserConn();
        for (ClientUser user : userMap.values()) {
            if (user.isValidate()) {
                ClientUser.UserConn userConn = user.getUserConn();
                clientUserConn.addCount(userConn.getConnCount());
                clientUserConn.getUserConnList().add(userConn);
            }
        }
        return clientUserConn;
    }
    
    /**
     * 对客户端进行广播
     * 
     * @param message
     * @param clientType
     * @since  1.0
     */
    public static void broadCast(IMProtoMessage<MessageLite> message, int clientFlag) {
        for (ClientUser user : userMap.values()) {
            if (user.isValidate()) {
                if (clientFlag == SysConstant.CLIENT_TYPE_FLAG_PC) {
                    user.broadcastWithOutMobile(message, null);
                } else if ((clientFlag == SysConstant.CLIENT_TYPE_FLAG_MOBILE)) {
                    user.broadcastToMobile(message, null);
                } else if ((clientFlag == SysConstant.CLIENT_TYPE_FLAG_BOTH)) {
                    user.broadcast(message, null);
                } else {
                    // 参数不正，不处理
                }
            }
        }
    }
    
    public static class ClientUserConn {
        private int totalCount = 0;
        private List<ClientUser.UserConn> userConnList = new ArrayList<>();
        /**
         * @return the totalCount
         */
        public int getTotalCount() {
            return totalCount;
        }
        /**
         * @param connCount
         * @since  1.0
         */
        public void addCount(int connCount) {
            this.totalCount += connCount;
        }
        /**
         * @param totalCount the totalCount to set
         */
        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
        /**
         * @return the userConnList
         */
        public List<ClientUser.UserConn> getUserConnList() {
            return userConnList;
        }
        /**
         * @param userConnList the userConnList to set
         */
        public void setUserConnList(List<ClientUser.UserConn> userConnList) {
            this.userConnList = userConnList;
        }
    }
    
}
