/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.PacketDecoder;
import com.blt.talk.common.code.PacketEncoder;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.handler.RouterConnectionHandler;
import com.blt.talk.message.server.manager.RouterHandlerManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component("RouterServerInitializer")
public class RouterServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private RouterHandlerManager routerHanderManager; 

    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
        pipeline.addLast("decoder", new PacketDecoder());
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("handler", new RouterConnectionHandler(this.routerHanderManager,
                applicationContext.getBean(RouterServerConnecter.class)));
    }

}
