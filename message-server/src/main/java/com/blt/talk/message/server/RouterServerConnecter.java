/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.message.server.config.RouterServerConfig;
import com.google.protobuf.MessageLite;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
@Scope(value =  ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RouterServerConnecter  {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RouterServerConfig routerServerConfig;

    private Bootstrap bootstrap;
    
    private Channel channel;
    private ChannelFuture future;
    private List<IMProtoMessage<MessageLite>> pooledMessageList = new ArrayList<>() ;

    @Autowired
    @Qualifier("RouterServerInitializer")
    private ChannelInboundHandler channelInboundHandler;
    
    private ChannelFutureListener channelFutureListener;
    
    public Channel channel() {
        return this.channel;
    }
    
    public boolean isClose = false;
    
    @PreDestroy
    public void destroy() {
        this.isClose = true;
        // 关闭现有的连接，关闭对应的线程
        if (future != null) {
            future.removeListener(channelFutureListener);
            future.channel().close();
            
        }
        logger.info("closed");
    }
    
    //  连接到服务端
    public void doConnect() {
        logger.debug( "doConnect");
        try {
            if (isClose) {
                return ;
            }
            synchronized(bootstrap) {
                if ((null == channel || (null != channel && !channel.isOpen()))
                        && null != this.routerServerConfig.getIp() && this.routerServerConfig.getPort() > 0) {
                    logger.debug( "连接Router服务");
                    this.future = bootstrap.connect(new InetSocketAddress(
                            this.routerServerConfig.getIp(), this.routerServerConfig.getPort()));
                    this.channel = future.channel();
                }
            }
        } catch (Exception e) {
            logger.error("连接Router服务失败", e);
            logger.debug( "关闭连接");
        }
    }
    
    public void initConnect() {
        
        NioEventLoopGroup group = new NioEventLoopGroup();

        // Client服务启动器 3.x的ClientBootstrap
        // 改为Bootstrap，且构造函数变化很大，这里用无参构造。
        bootstrap = new Bootstrap();
        // 指定EventLoopGroup
        bootstrap.group(group);
        // 指定channel类型
        bootstrap.channel(NioSocketChannel.class);
        // 指定Handler
        bootstrap.handler(channelInboundHandler);
        // 设置TCP协议的属性
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        channelFutureListener = new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                // logger.debug(Config.TAG, "isDone:" + f.isDone() + " isSuccess:" + f.isSuccess() +
                // " cause" + f.cause() + " isCancelled" + f.isCancelled());
                if (f.isSuccess()) {
                    logger.debug("重新连接服务器成功");
                } else {
                    logger.debug("重新连接服务器失败");
                    
                    f.channel().eventLoop().shutdownGracefully();
                    
                    // 3秒后重新连接
                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        };
    }
    
    /**
     * 往Router服务器发送消息
     * @param message
     * @since  1.0
     */
    public void send(IMProtoMessage<MessageLite> message) {
        this.send(message, false);
    }
    
    /**
     * 往Router服务器发送消息
     * 
     * @param message 消息
     * @param iswait 是否等连接恢复了再发
     * @since  1.0
     */
    public void send(IMProtoMessage<MessageLite> message, boolean iswait) {
        if (this.channel != null && this.channel.isActive()) {
            synchronized (pooledMessageList) {
                if (!pooledMessageList.isEmpty()) {
                    for (IMProtoMessage<MessageLite> pooledMessageList: pooledMessageList) {
                        this.channel.writeAndFlush(pooledMessageList);
                    }
                    pooledMessageList.clear();
                }
            }
            this.channel.writeAndFlush(message);
        } else {
            if (iswait) {
                pooledMessageList.add(message);
            }
        }
    }
    @Async
    public void start(String... args) throws Exception {

        // 尝试连接Router服务
        try {
            logger.info("尝试连接Router服务...");
            this.initConnect();
            this.doConnect();
        } finally {
            logger.info("尝试连接Router服务...");
        }
    }

}
