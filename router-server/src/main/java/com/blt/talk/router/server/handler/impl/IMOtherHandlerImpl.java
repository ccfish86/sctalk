/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.ServerUserStat;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.router.server.handler.IMOtherHandler;
import com.blt.talk.router.server.manager.ClientConnection;
import com.blt.talk.router.server.manager.ClientConnectionMap;
import com.blt.talk.router.server.manager.MessageServerManager;
import com.blt.talk.router.server.manager.UserClientInfoManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMOtherHandlerImpl implements IMOtherHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void hearBeat(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // 心跳处理
    }

    @Override
    public void onlineUserInfo(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMServer.IMOnlineUserInfo onlineUserInfo = (IMServer.IMOnlineUserInfo) body;
        
        logger.debug("更新用户信息, user_cnt={}", onlineUserInfo.getUserStatListCount());
        
        Long netId = ctx.attr(ClientConnection.NETID).get();
        MessageServerManager.update(netId, onlineUserInfo.getUserStatListCount());
        
        // _UpdateUserStatus
        for (ServerUserStat userStat: onlineUserInfo.getUserStatListList()) {
            UserClientInfoManager.UserClientInfo userClientInfo = UserClientInfoManager.getUserInfo(userStat.getUserId());
            
            if (userClientInfo != null) {
                // 现存连接
                if (userClientInfo.findRouteConn(netId)) {
                    if (userStat.getStatus() != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                        userClientInfo.removeClientType(userStat.getClientType());
                        
                        if (userClientInfo.isMsgConnNULL()) {
                            userClientInfo.removeRouteConn(netId);
                            if (userClientInfo.getRouteConnCount() == 0) {
                                UserClientInfoManager.erase(userStat.getUserId());
                            }
                        }
                    } else {
                        userClientInfo.addClientType(userStat.getClientType());
                    }
                } else {
                    if (userStat.getStatus() != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                        userClientInfo.addClientType(userStat.getClientType());
                        userClientInfo.addRouteConn(netId);
                    }
                }
                
            } else {
                if (userStat.getStatus() != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo = new UserClientInfoManager.UserClientInfo();
                    userClientInfo.addClientType(userStat.getClientType());
                    userClientInfo.addRouteConn(netId);
                    
                    UserClientInfoManager.insert(userStat.getUserId(), userClientInfo);
                }
            }
        }
        
    }

    @Override
    public void onlineUserStatus(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMServer.IMUserStatusUpdate userStatusUpdate = (IMServer.IMUserStatusUpdate) body;
        
        logger.debug("更新用户在线状态, user_id={}", userStatusUpdate.getUserId());
        
        Long netId = ctx.attr(ClientConnection.NETID).get();
        
        // 更新用户状态
        this.updateUserStat(netId, userStatusUpdate.getUserId(),
                IMBaseDefine.UserStatType.forNumber(userStatusUpdate.getUserStatus()),
                userStatusUpdate.getClientType());
        
        //FIXME
        // 用于通知客户端,同一用户在pc端的登录情况
        // 暂时 不对外发布PC版
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.router.server.handler.IMOtherHandler#roleSet(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void roleSet(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMServer.IMRoleSet roleSet = (IMServer.IMRoleSet) body;
        
        Long netId = ctx.attr(ClientConnection.NETID).get();
        ClientConnection conn = ClientConnectionMap.getClientConnection(netId);
        
        conn.setMaster(roleSet.getMaster() == 1);
    }
    
    protected void updateUserStat(Long netId, Long userId, IMBaseDefine.UserStatType status, IMBaseDefine.ClientType clientType) {
        
        UserClientInfoManager.UserClientInfo userClientInfo = UserClientInfoManager.getUserInfo(userId);
        
        if (userClientInfo != null) {
            // 现存连接
            if (userClientInfo.findRouteConn(netId)) {
                if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo.removeClientType(clientType);
                    
                    if (userClientInfo.isMsgConnNULL()) {
                        userClientInfo.removeRouteConn(netId);
                        if (userClientInfo.getRouteConnCount() == 0) {
                            UserClientInfoManager.erase(userId);
                        }
                    }
                } else {
                    userClientInfo.addClientType(clientType);
                }
            } else {
                if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo.addClientType(clientType);
                    userClientInfo.addRouteConn(netId);
                }
            }
            
        } else {
            if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                userClientInfo = new UserClientInfoManager.UserClientInfo();
                userClientInfo.addClientType(clientType);
                userClientInfo.addRouteConn(netId);
                
                UserClientInfoManager.insert(userId, userClientInfo);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.blt.talk.router.server.handler.IMOtherHandler#updateMessageServer(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void updateMessageServer(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        logger.info("更新消息服务器信息");
        IMServer.IMMsgServInfo messageServerInfo = (IMServer.IMMsgServInfo) body;
        Long netId = ctx.attr(ClientConnection.NETID).get();
        
        MessageServerManager.MessageServerInfo serverInfo = new MessageServerManager.MessageServerInfo();
        serverInfo.setIp(messageServerInfo.getIp1());
        serverInfo.setPort(messageServerInfo.getPort());
        serverInfo.setUserCount(messageServerInfo.getCurConnCnt());
        MessageServerManager.insert(netId, serverInfo);
    }
}
