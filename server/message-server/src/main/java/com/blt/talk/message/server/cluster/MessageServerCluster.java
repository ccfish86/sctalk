/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.blt.talk.common.code.DefaultIMHeader;
import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.AVCallCmdId;
import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMBuddy.IMUserStatNotify;
import com.blt.talk.common.code.proto.IMServer.IMServerPCLoginStatusNotify;
import com.blt.talk.common.code.proto.IMWebRTC.IMAVCallCancelReq;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.message.server.MessageServerStarter;
import com.blt.talk.message.server.cluster.UserClientInfoManager.IMAVCall;
import com.blt.talk.message.server.cluster.UserClientInfoManager.UserClientInfo;
import com.blt.talk.message.server.cluster.task.MessageToUserTask;
import com.blt.talk.message.server.manager.ClientUser;
import com.google.protobuf.MessageLite;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.topic.ITopic;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理消息服务器管理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class MessageServerCluster implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HazelcastInstance hazelcastInstance;
    @Autowired
    private MyClusterMessageListener clusterMessageListener;
    @Autowired
    private MyClusterMembershipListener membershipListener;
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
        MyClusterMessage clusterMessage = new MyClusterMessage(header, messageLit);
        this.topic.publish(clusterMessage);
    }

    @Async
    public void sendToUser(long userId, long toNetId, IMHeader header, MessageLite messageLit) {

        String memberUuid = messageServerManager.getMemberByNetId(toNetId);
        Member member = toMember(memberUuid);

        if (member != null) {

            MessageToUserTask command = new MessageToUserTask(userId, toNetId,
                    new MyClusterMessage(header, messageLit));
            hazelcastInstance.getExecutorService("default").submitToMember(command, member);
        }
    }

    @Async
    public void sendToUser(long userId, IMHeader header, MessageLite messageLit) {

        UserClientInfo clientInfo = userClientInfoManager.getUserInfo(userId);

        if (clientInfo != null) {
            Set<String> uuids = messageServerManager.getMemberByNetIds(clientInfo.getRouteConns());
            List<Member> members = toMembers(uuids);

            if (members != null) {

                MessageToUserTask command = new MessageToUserTask(userId, null,
                        new MyClusterMessage(header, messageLit));
                hazelcastInstance.getExecutorService("default").submitToMembers(command, members);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.topic = hazelcastInstance.getTopic("message-server#router");
        this.topic.addMessageListener(clusterMessageListener);
        hazelcastInstance.getCluster().addMembershipListener(membershipListener);
    }

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param clientType 客户端类型
     * @param msgCtx 用户与消息服务器的Netty连接
     * @param status 状态
     * @return
     * @since 1.0
     */
    @Async
    public ListenableFuture<?> userStatusUpdate(Long userId, ChannelHandlerContext msgCtx,
            UserStatType status) {

        ClientType clientType = msgCtx.attr(ClientUser.CLIENT_TYPE).get();
        long handleId = msgCtx.attr(ClientUser.HANDLE_ID).get();
        UUID nodeId = hazelcastInstance.getCluster().getLocalMember().getUuid();
        
        String userStatusLockKey = "user_status_" + userId;
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(userStatusLockKey);
        
        try {
            // 用户状态改变，同期处理，避免MAP更新时出现脏数据
            lock.lock();
            
            UserClientInfoManager.UserClientInfo userClientInfo =
                    userClientInfoManager.getUserInfo(userId);
            
            // 处理服务器与连接关联
            if (status == IMBaseDefine.UserStatType.USER_STATUS_ONLINE) {
                messageServerManager.addConnect(nodeId.toString(), handleId);
            } else {
                messageServerManager.removeConnect(nodeId.toString(), handleId);
            }
    
            if (userClientInfo != null) {
                // 现存连接
                if (userClientInfo.findRouteConn(handleId)) {
                    if (status != IMBaseDefine.UserStatType.USER_STATUS_ONLINE) {
                        userClientInfo.removeClient(clientType, handleId);
                        userClientInfoManager.erase(userId, handleId);
                    } else {
                        userClientInfo.addClientType(clientType);
                    }
                } else {
                    if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                        userClientInfo.addClientType(clientType);
                        userClientInfo.addRouteConn(handleId);
                    }
                }
                userClientInfoManager.update(userId, userClientInfo);
                
                
            } else {
                if (status != IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    userClientInfo = new UserClientInfoManager.UserClientInfo();
                    userClientInfo.addClientType(clientType);
                    userClientInfo.addRouteConn(handleId);
                    // userClientInfo.setUuid(nodeId);
                    userClientInfo.setUserId(userId);
    
                    userClientInfoManager.insert(userId, userClientInfo);
                }
            }

            userClientInfo = userClientInfoManager.getUserInfo(userId);

            // 用于通知客户端,同一用户在pc端的登录情况
            if (userClientInfo != null) {
                IMHeader header = new DefaultIMHeader(IMBaseDefine.ServiceID.SID_OTHER_VALUE,
                        IMBaseDefine.OtherCmdID.CID_OTHER_LOGIN_STATUS_NOTIFY_VALUE);
                IMServerPCLoginStatusNotify.Builder pcLoginStatusNotifyBuilder = 
                        IMServerPCLoginStatusNotify.newBuilder().setUserId(userId);
                if (status == IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                    pcLoginStatusNotifyBuilder.setLoginStatus(0);
                    
                    // pc端下线且无pc端存在，则给msg_server发送一个通知
                    if (CommonUtils.isPc(clientType) && !userClientInfo.isPCClientLogin()) {
                        send(header, pcLoginStatusNotifyBuilder.build());
                    }
                } else {
                    // 在线
                    pcLoginStatusNotifyBuilder.setLoginStatus(1);
                    if (userClientInfo.isPCClientLogin()) {
                        send(header, pcLoginStatusNotifyBuilder.build());
                    }
                }
            }
            
            // 状态更新的是pc client端，则通知给所有其他人
            if (CommonUtils.isPc(clientType)) {
                IMHeader header = new DefaultIMHeader(IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE,
                        IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE);
                IMBaseDefine.UserStat userStat = userStat(userId, status);
                IMUserStatNotify userStatNotify = IMUserStatNotify.newBuilder().setUserStat(userStat).build();
                
                if (userClientInfo != null) {
                    //如果是pc客户端离线，但是仍然存在pc客户端，则不发送离线通知
                    //此种情况一般是pc客户端多点登录时引起
                    if (status == IMBaseDefine.UserStatType.USER_STATUS_OFFLINE && userClientInfo.isPCClientLogin()) {
                        // 不发送离线通知
                    } else {
                        send(header, userStatNotify);
                    }
                } else {
                    send(header, userStatNotify);
                }
            }

        } finally {
            lock.unlock();
        }

        AsyncResult<?> result = new AsyncResult<>(nodeId);

        return result;
    }

    /**
     * 踢掉用户连接 <br>
     * 限制多设备同时连接
     * 
     * @param userId 用户ID
     * @param reasonType 原因
     * @return
     * @since 1.0
     */
    @Async
    public ListenableFuture<List<IMBaseDefine.UserStat>> kickOutSameClientType(Long userId,
            Integer reasonType) {
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
     * @since 1.0
     */
    @Async
    public ListenableFuture<List<IMBaseDefine.UserStat>> userStatusReq(Long fromUserId,
            List<Long> userIdList) {

        logger.debug("查询用户在线状态, user_cnt={}", userIdList.size());

        List<IMBaseDefine.UserStat> userStatList = new ArrayList<>();
        for (Long userId : userIdList) {

            UserClientInfoManager.UserClientInfo userClientInfo =
                    userClientInfoManager.getUserInfo(userId);
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
     * 查询用户在线状态
     * 
     * @param userId 用户ID
     * @return 用户状态
     * @since 1.1
     */
    @Async
    public ListenableFuture<IMBaseDefine.UserStat> userStatusReq(Long userId) {

        logger.debug("查询用户在线状态, user_id={}", userId);

        UserClientInfoManager.UserClientInfo userClientInfo =
                userClientInfoManager.getUserInfo(userId);
        IMBaseDefine.UserStat.Builder userStatBuiler = IMBaseDefine.UserStat.newBuilder();
        userStatBuiler.setUserId(userId);
        if (userClientInfo != null) {
            userStatBuiler.setStatus(userClientInfo.getStatus());
        } else {
            userStatBuiler.setStatus(IMBaseDefine.UserStatType.USER_STATUS_OFFLINE);
        }

        AsyncResult<IMBaseDefine.UserStat> result = new AsyncResult<>(userStatBuiler.build());
        return result;
    }

    /**
     * @param messageServerStarter
     * @since 1.0
     */
    @Async
    public void registLocal(MessageServerStarter messageServerStarter) {

        logger.info("更新消息服务器信息");

        MessageServerManager.MessageServerInfo serverInfo =
                new MessageServerManager.MessageServerInfo();
        serverInfo.setPriorIP(messageServerStarter.getPriorIP());
        serverInfo.setIp(messageServerStarter.getIpadress());
        serverInfo.setPort(messageServerStarter.getPort());
        messageServerManager.insert(serverInfo);
    }

    @Async
    public ListenableFuture<?> webrtcInitateCallReq(long fromId, long toId, long netId) {

        // FIXME
        // 从当前的通话中查看是否已存在
        // 如果存在，判断类型，返回给呼叫发起方
        IMAVCall avCall = userClientInfoManager.getCaller(fromId);

        // 如果不存在，则处理呼叫
        if (avCall == null) {
            avCall = userClientInfoManager.getCaller(toId);
            if (avCall != null) {
                // TODO Peer Busy
                return AsyncResult.forExecutionException(new Exception());
            }
            userClientInfoManager.addCaller(fromId, netId);
        } else {
            // TODO Self Busy

            return AsyncResult.forExecutionException(new Exception());
        }

        return AsyncResult.forValue("");
    }

    @Async
    public ListenableFuture<?> webrtcInitateCallRes(long fromId, long toId, long netId) {

        // FIXME
        // 从当前的通话中查看是否已存在
        IMAVCall toAvCall = userClientInfoManager.getCalled(toId);
        if (toAvCall != null) {
            // 如果存在，返回给呼叫发起方
            // TODO 其他端，已经接受了 IMAVCallCancelReq
            return AsyncResult.forExecutionException(new Exception());
        }
        
        // 如果不存在，则处理呼叫
        userClientInfoManager.addCalled(toId, netId);
        return AsyncResult.forValue("");
    }

    @Async
    public void webrtcHungupReq(long fromId, long toId, long callId) {

        // 从当前的通话中查看是否已存在
        IMAVCall avCall = userClientInfoManager.getCaller(fromId);
        Long toNetId;

        if (avCall == null) {
            // 对方信息
            IMAVCall avToCall = userClientInfoManager.getCaller(toId);
            // avToCall.getNetId()
            toNetId = avToCall.getNetId();
            userClientInfoManager.removeCalled(fromId);
        } else {
            IMAVCall avToCall = userClientInfoManager.getCalled(toId);
            // avToCall.getNetId()
            toNetId = avToCall.getNetId();
            userClientInfoManager.removeCaller(fromId);
        }
        IMAVCallCancelReq callCancelReq = IMAVCallCancelReq.newBuilder().setFromId(fromId)
                .setToId(toId).setCallId(callId).build();

        IMHeader hdCancel = new IMHeader();
        hdCancel.setServiceId(ServiceID.SID_AVCALL_VALUE);
        hdCancel.setCommandId(AVCallCmdId.CID_AVCALL_CANCEL_REQ_VALUE);

        // 把挂断的消息，广播给对方
        sendToUser(toId, toNetId, hdCancel, callCancelReq);
    }

    private Member toMember(String uuid) {
        if (uuid != null) {
            Set<Member> members = hazelcastInstance.getCluster().getMembers();
            for (Member member : members) {
                if (uuid.equals(member.getUuid())) {
                    return member;
                }
            }
        }
        return null;
    }

    private List<Member> toMembers(Set<String> uuids) {
        if (uuids != null) {
            Set<Member> members = hazelcastInstance.getCluster().getMembers();
            List<Member> rmembers = new ArrayList<>();
            for (Member member : members) {
                if (uuids.contains(member.getUuid())) {
                    rmembers.add(member);
                }
            }
            return rmembers;
        }
        return null;
    }
    
    private IMBaseDefine.UserStat userStat(long userId, UserStatType status) {
        IMBaseDefine.UserStat.Builder userStatBuiler = IMBaseDefine.UserStat.newBuilder();
        userStatBuiler.setUserId(userId);
        userStatBuiler.setStatus(status);
        return userStatBuiler.build();
    }
}
