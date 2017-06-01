/*
	 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.PacketDecoder;
import com.blt.talk.common.code.PacketEncoder;
import com.blt.talk.router.server.handler.RouterServerHandler;
import com.blt.talk.router.server.manager.HandlerManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 消息通道初始化类
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class NettyRouterServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private HandlerManager handlerManager;
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 手动处理数据包的长度
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
        pipeline.addLast("decoder", new PacketDecoder());
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("handler", new RouterServerHandler(handlerManager));

    }

}
