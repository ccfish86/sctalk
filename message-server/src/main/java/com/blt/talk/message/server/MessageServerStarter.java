/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.analysis.ProtobufParseMap;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.FileCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMFile;
import com.blt.talk.common.code.proto.IMGroup;
import com.blt.talk.common.code.proto.IMLogin;
import com.blt.talk.common.code.proto.IMMessage;
import com.blt.talk.common.code.proto.IMOther;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.message.server.channel.NettyChatServerInitializer;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.config.MessageServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
@Configuration
public class MessageServerStarter {

    private static Logger logger = LoggerFactory.getLogger(MessageServerStarter.class);

    @Autowired
    @Qualifier("NettyChatServerInitializer")
    private NettyChatServerInitializer channelInboundHandler;

    private ServerBootstrap sBootstrap;
    private ChannelFuture future;

    @Autowired
    private MessageServerConfig messageServerConfig;
    @Autowired
    private MessageServerCluster messageServerCluster;
    
    private boolean started = false;
    
    /** IP */
    private String ipadress;
    /** 端口号 */
    private int port;

    @PreDestroy
    public void destroy() {
        
        // FIXME 处理资源关闭，后期需要对应：
        if (future != null && future.channel() != null) {
            // 1. 通知在线用户[服务器即将关闭]
            // 2. 关闭现有的连接，关闭对应的线程
            if (future.channel().isOpen()) {
                future.channel().close();
            }
        }
        logger.info("closed");
    }

    @Async
    public void start(String... args) throws Exception {
        // 初始化Message对象
        this.initProtobuf();
        // 初始化连接对象
        this.start();
    }
    
    private void start() throws InterruptedException, IOException {
        // NioEventLoopGroup是用来处理IO操作的多线程事件循环器
        // boss用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用来处理已经被接收的连接;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

            // 是一个启动NIO服务的辅助启动类
            sBootstrap = new ServerBootstrap();
            // These EventLoopGroup's are used to handle all the events and IO for ServerChannel
            // and
            // Channel's.
            // 为bootstrap设置acceptor的EventLoopGroup和client的EventLoopGroup
            // 这些EventLoopGroups用于处理所有的IO事件
            // ?这里为什么设置两个group呢?
            sBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(channelInboundHandler).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

            
            // 绑定端口,开始接收进来的连接
            future = sBootstrap.bind(messageServerConfig.getIp(), messageServerConfig.getPort()).sync();

            // 获取绑定的端口号
            if (future.channel().localAddress() instanceof InetSocketAddress ) {
                InetSocketAddress socketAddress = (InetSocketAddress)future.channel().localAddress();
                this.ipadress = socketAddress.getAddress().getHostAddress();
                this.port = socketAddress.getPort();
                this.started = true;
                logger.info("NettyChatServer 启动了，address={}:{}", socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
            }
            
            // messageServerCluster
            messageServerCluster.registLocal(this);
            
            // 等待服务器socket关闭
            // 在本例子中不会发生,这时可以关闭服务器了
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            logger.info("NettyChatServer 关闭了");
        }
    }

    /**
     * 初始化Protobuf参数
     * 
     * @since 1.0
     */
    private void initProtobuf() {

        // GroupCmdID
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_NORMAL_LIST_REQUEST_VALUE,
                IMGroup.IMNormalGroupListReq::parseFrom, IMGroup.IMNormalGroupListReq.class);
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_INFO_REQUEST_VALUE,
                IMGroup.IMGroupInfoListReq::parseFrom, IMGroup.IMGroupInfoListReq.class);
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_CREATE_REQUEST_VALUE,
                IMGroup.IMGroupCreateReq::parseFrom, IMGroup.IMGroupCreateReq.class);
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_CHANGE_MEMBER_REQUEST_VALUE,
                IMGroup.IMGroupChangeMemberReq::parseFrom, IMGroup.IMGroupChangeMemberReq.class);
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_SHIELD_GROUP_REQUEST_VALUE,
                IMGroup.IMGroupShieldReq::parseFrom, IMGroup.IMGroupShieldReq.class);
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE,
                IMGroup.IMGroupChangeMemberNotify::parseFrom, IMGroup.IMGroupChangeMemberNotify.class);

        // OtherCmdID
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_HEARTBEAT_VALUE,
                IMOther.IMHeartBeat::parseFrom, IMOther.IMHeartBeat.class);
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE,
                IMServer.IMServerKickUser::parseFrom, IMServer.IMServerKickUser.class); 
        
        // CID_SWITCH_P2P_CMD
        ProtobufParseMap.register(ServiceID.SID_SWITCH_SERVICE_VALUE, SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE,
                IMSwitchService.IMP2PCmdMsg::parseFrom, IMSwitchService.IMP2PCmdMsg.class);

        // MessageCmdID
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_DATA_VALUE,
                IMMessage.IMMsgData::parseFrom, IMMessage.IMMsgData.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_DATA_ACK_VALUE,
                IMMessage.IMMsgDataAck::parseFrom, IMMessage.IMMsgDataReadAck.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_READ_ACK_VALUE,
                IMMessage.IMMsgDataReadAck::parseFrom, IMMessage.IMMsgDataReadAck.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_READ_NOTIFY_VALUE,
                IMMessage.IMMsgDataReadNotify::parseFrom, IMMessage.IMMsgDataReadNotify.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_TIME_REQUEST_VALUE,
                IMMessage.IMClientTimeReq::parseFrom, IMMessage.IMClientTimeReq.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_UNREAD_CNT_REQUEST_VALUE,
                IMMessage.IMUnreadMsgCntReq::parseFrom, IMMessage.IMUnreadMsgCntReq.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_LIST_REQUEST_VALUE,
                IMMessage.IMGetMsgListReq::parseFrom, IMMessage.IMGetMsgListReq.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_REQ_VALUE,
                IMMessage.IMGetLatestMsgIdReq::parseFrom, IMMessage.IMGetLatestMsgIdReq.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_GET_BY_MSG_ID_REQ_VALUE,
                IMMessage.IMGetMsgByIdReq::parseFrom, IMMessage.IMGetMsgByIdReq.class);

        // LoginCmdID
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_MSGSERVER_VALUE,
                IMLogin.IMMsgServReq::parseFrom, IMLogin.IMMsgServReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE,
                IMLogin.IMLoginReq::parseFrom, IMLogin.IMLoginReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_LOGINOUT_VALUE,
                IMLogin.IMLogoutReq::parseFrom, IMLogin.IMLogoutReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_KICK_USER_VALUE,
                IMLogin.IMKickUser::parseFrom, IMLogin.IMKickUser.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_DEVICETOKEN_VALUE,
                IMLogin.IMDeviceTokenReq::parseFrom, IMLogin.IMDeviceTokenReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_KICKPCCLIENT_VALUE,
                IMLogin.IMKickPCClientReq::parseFrom, IMLogin.IMKickPCClientReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_PUSH_SHIELD_VALUE,
                IMLogin.IMPushShieldReq::parseFrom, IMLogin.IMPushShieldReq.class);
        ProtobufParseMap.register(ServiceID.SID_LOGIN_VALUE, LoginCmdID.CID_LOGIN_REQ_QUERY_PUSH_SHIELD_VALUE,
                IMLogin.IMQueryPushShieldReq::parseFrom, IMLogin.IMQueryPushShieldReq.class);

        // BuddyListCmd
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST_VALUE,
                IMBuddy.IMRecentContactSessionReq::parseFrom, IMBuddy.IMRecentContactSessionReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE, BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE,
                IMBuddy.IMUserStatNotify::parseFrom, IMBuddy.IMUserStatNotify.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE, BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE,
                IMBuddy.IMUsersInfoReq::parseFrom, IMBuddy.IMUsersInfoReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE, IMBuddy.IMRemoveSessionReq::parseFrom,
                IMBuddy.IMRemoveSessionReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE, BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE,
                IMBuddy.IMAllUserReq::parseFrom, IMBuddy.IMAllUserReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE, IMBuddy.IMUsersStatReq::parseFrom,
                IMBuddy.IMUsersStatReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE, IMBuddy.IMUsersStatRsp::parseFrom,
                IMBuddy.IMUsersStatRsp.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE, IMBuddy.IMChangeAvatarReq::parseFrom,
                IMBuddy.IMChangeAvatarReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE, IMBuddy.IMPCLoginStatusNotify::parseFrom,
                IMBuddy.IMPCLoginStatusNotify.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE, IMBuddy.IMRemoveSessionNotify::parseFrom,
                IMBuddy.IMRemoveSessionNotify.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE, IMBuddy.IMDepartmentReq::parseFrom,
                IMBuddy.IMDepartmentReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE, IMBuddy.IMAvatarChangedNotify::parseFrom,
                IMBuddy.IMAvatarChangedNotify.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_REQUEST_VALUE, IMBuddy.IMChangeSignInfoReq::parseFrom,
                IMBuddy.IMChangeSignInfoReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE,
                IMBuddy.IMSignInfoChangedNotify::parseFrom, IMBuddy.IMSignInfoChangedNotify.class);
        
        // FileCmdID
        ProtobufParseMap.register(ServiceID.SID_FILE_VALUE,
                FileCmdID.CID_FILE_HAS_OFFLINE_REQ_VALUE,
                IMFile.IMFileHasOfflineReq::parseFrom, IMFile.IMFileHasOfflineReq.class);
        
    }

    /**
     * @return the ipadress
     */
    public String getIpadress() {
        return ipadress;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * 
     * @return the server started or not
     */
    public boolean isStarted() {
        return started;
    }

}
