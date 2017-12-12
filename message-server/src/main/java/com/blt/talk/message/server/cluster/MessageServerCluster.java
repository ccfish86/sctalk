/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.message.server.MessageServerStarter;
import com.blt.talk.message.server.manager.ClientUser;
import com.google.protobuf.MessageLite;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理消息服务器管理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class MessageServerCluster implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private HazelcastInstance hazelcastInstance;
    @Autowired
    private MyClusterMessageListener clusterMessageListener;
    @Autowired
    private UserClientInfoManager userClientInfoManager;
    @Autowired
    private MessageServerManager messageServerManager;
    
    private ITopic<MyClusterMessage> topic;
    
    public void send(IMHeader header, MessageLite messageLite) {
        send(header, messageLite, false);
    }

    public void send(IMHeader header, MessageLite messageLit, boolean retry) {
        // 根据类型处理
        MyClusterMessage clusterMessage = new MyClusterMessage();
        this.topic.publish(clusterMessage);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.topic =  hazelcastInstance.getTopic("message-server#router");
        this.topic.addMessageListener(clusterMessageListener);
    }
    
    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param msgCtx  用户与消息服务器的Netty连接
     * @param status 状态
     * @return
     * @since  1.0
     */
    @Async
    public ListenableFuture<?> userStatusUpdate(Long userId, ChannelHandlerContext msgCtx, UserStatType status) {

        UserClientInfoManager.UserClientInfo userClientInfo = userClientInfoManager.getUserInfo(userId);
        ClientType clientType =  msgCtx.attr(ClientUser.CLIENT_TYPE).get();
        String nodeId = hazelcastInstance.getCluster().getLocalMember().getUuid();
        
        if (userClientInfo != null) {
            // 现存连接
            if (userClientInfo.findRouteConn(nodeId)) {
                if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo.removeClientType(clientType);
                    
                    if (userClientInfo.isMsgConnNULL()) {
                        userClientInfo.removeRouteConn(nodeId);
                        if (userClientInfo.getRouteConnCount() == 0) {
                            userClientInfoManager.erase(userId);
                        }
                    }
                } else {
                    userClientInfo.addClientType(clientType);
                }
            } else {
                if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo.addClientType(clientType);
                    userClientInfo.addRouteConn(nodeId);
                }
            }
            
        } else {
            if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                userClientInfo = new UserClientInfoManager.UserClientInfo();
                userClientInfo.addClientType(clientType);
                userClientInfo.addRouteConn(nodeId);
                userClientInfo.setUserId(userId);
                
                userClientInfoManager.insert(userId, userClientInfo);
            }
        }
        
        AsyncResult<?> result = new AsyncResult<>(nodeId);
        
        return result; 
    }
    /**
     * 踢掉用户连接
     * <br>
     * 限制多设备同时连接
     * 
     * @param userId 用户ID
     * @param reasonType 原因
     * @return
     * @since  1.0
     */
    @Async
    public ListenableFuture<List<IMBaseDefine.UserStat>> kickOutSameClientType(Long userId, Integer reasonType) {
        // 更新用户在线状态
        // FIXME
        return null;
    }
    
    /**
     * 查询用户在线状态
     * 
     * @param fromUserId 用户ID
     * @param userIdList 查询列表
     * @return
     * @since  1.0
     */
    @Async
    public ListenableFuture<List<IMBaseDefine.UserStat>> userStatusReq(Long fromUserId, List<Long> userIdList) {
        
        logger.debug("查询用户在线状态, user_cnt={}", userIdList.size());
        
        List<IMBaseDefine.UserStat> userStatList = new ArrayList<>();
        for (Long userId: userIdList) {
            
            UserClientInfoManager.UserClientInfo userClientInfo = userClientInfoManager.getUserInfo(userId);
            IMBaseDefine.UserStat.Builder userStatBuiler = IMBaseDefine.UserStat.newBuilder();
            userStatBuiler.setUserId(userId);
            if (userClientInfo != null) {
                userStatBuiler.setStatus(userClientInfo.getStatus());
            } else {
                userStatBuiler.setStatus(IMBaseDefine.UserStatType.USER_STATUS_OFFLINE);
            }
            
            userStatList.add(userStatBuiler.build());
        }
        
        AsyncResult<List<IMBaseDefine.UserStat>> result = new AsyncResult<>(userStatList);
        return result;
    }

    /**
     * @param messageServerStarter
     * @since  1.0
     */
    @Async
    public void registLocal(MessageServerStarter messageServerStarter) {

        logger.info("更新消息服务器信息");
        
        MessageServerManager.MessageServerInfo serverInfo = new MessageServerManager.MessageServerInfo();
        serverInfo.setIp(messageServerStarter.getIpadress());
        serverInfo.setPort(messageServerStarter.getPort());
        serverInfo.setUserCount(0);
        messageServerManager.insert(serverInfo);
    }

}
