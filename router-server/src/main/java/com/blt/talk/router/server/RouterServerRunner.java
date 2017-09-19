/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.analysis.ProtobufParseMap;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMGroup;
import com.blt.talk.common.code.proto.IMMessage;
import com.blt.talk.common.code.proto.IMOther;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.router.server.config.RouterServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

/**
 * Router服务启动程序
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
@Configuration
public class RouterServerRunner implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(RouterServerRunner.class);

    @Autowired
    private ChannelInboundHandler channelInboundHandler;

    private ServerBootstrap sBootstrap;
    private ChannelFuture future;

    @Autowired
    private RouterServerConfig routerServerConfig;

    /**
     * 在服务销毁时的处理
     * 
     * @since  1.0
     */
    @PreDestroy
    public void destroy() {

        // FIXME 处理资源关闭，后期需要对应：
        // 1. 通知在线用户[服务器即将关闭]
        // 2. 关闭现有的连接，关闭对应的线程
        future.channel().close();
        logger.info("closed");
    }

    @Override
    public void run(String...args) throws InterruptedException {
        // NioEventLoopGroup是用来处理IO操作的多线程事件循环器
        // boss用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用来处理已经被接收的连接;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 初始化Message对象
            this.initProtobuf();

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
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            logger.info("NettyRouterServer 启动了");

            // 绑定端口,开始接收进来的连接
            future = sBootstrap.bind(routerServerConfig.getIp(), routerServerConfig.getPort()).sync();

            // 获取绑定的端口号
            if (future.channel().localAddress() instanceof InetSocketAddress ) {
                InetSocketAddress socketAddress = (InetSocketAddress)future.channel().localAddress();
                logger.info("NettyChatServer 启动了，address={}:{}", socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
            }            // 等待服务器socket关闭
            // 在本例子中不会发生,这时可以关闭服务器了
            future.channel().closeFuture().sync();
        } finally {
            //
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            logger.info("NettyRouterServer 关闭了");
        }
    }

    /**
     * 初始化Protobuf参数
     * <br>
     * 仅需要注册当前Router能够处理的Command
     * 
     * @since 1.0
     */
    void initProtobuf() {

        // GroupCmdID
        ProtobufParseMap.register(ServiceID.SID_GROUP_VALUE, GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE,
                IMGroup.IMGroupChangeMemberNotify::parseFrom, IMGroup.IMGroupChangeMemberNotify.class);

        // OtherCmdID
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_HEARTBEAT_VALUE,
                IMOther.IMHeartBeat::parseFrom, IMOther.IMHeartBeat.class);
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_ROLE_SET_VALUE,
                IMServer.IMRoleSet::parseFrom, IMServer.IMRoleSet.class);
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_ONLINE_USER_INFO_VALUE,
                IMServer.IMOnlineUserInfo::parseFrom, IMServer.IMOnlineUserInfo.class);
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_USER_STATUS_UPDATE_VALUE,
                IMServer.IMUserStatusUpdate::parseFrom, IMServer.IMUserStatusUpdate.class);
        // CID_OTHER_USER_CNT_UPDATE
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE,
                IMServer.IMUserCntUpdate::parseFrom, IMServer.IMUserCntUpdate.class);
        
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE,
                IMServer.IMServerKickUser::parseFrom, IMServer.IMServerKickUser.class);
        ProtobufParseMap.register(ServiceID.SID_OTHER_VALUE, OtherCmdID.CID_OTHER_MSG_SERV_INFO_VALUE,
                IMServer.IMMsgServInfo::parseFrom, IMServer.IMMsgServInfo.class);

        // CID_SWITCH_P2P_CMD
        ProtobufParseMap.register(ServiceID.SID_SWITCH_SERVICE_VALUE, SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE,
                IMSwitchService.IMP2PCmdMsg::parseFrom, IMSwitchService.IMP2PCmdMsg.class);

        // MessageCmdID
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_DATA_VALUE,
                IMMessage.IMMsgData::parseFrom, IMMessage.IMMsgData.class);
        ProtobufParseMap.register(ServiceID.SID_MSG_VALUE, MessageCmdID.CID_MSG_READ_NOTIFY_VALUE,
                IMMessage.IMMsgDataReadNotify::parseFrom, IMMessage.IMMsgDataReadNotify.class);


        // BuddyListCmd
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE, IMBuddy.IMUsersStatReq::parseFrom,
                IMBuddy.IMUsersStatReq.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE, IMBuddy.IMRemoveSessionNotify::parseFrom,
                IMBuddy.IMRemoveSessionNotify.class);
        ProtobufParseMap.register(ServiceID.SID_BUDDY_LIST_VALUE,
                BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE,
                IMBuddy.IMSignInfoChangedNotify::parseFrom, IMBuddy.IMSignInfoChangedNotify.class);

    }

}
