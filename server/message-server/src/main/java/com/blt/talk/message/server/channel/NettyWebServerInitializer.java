/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.PacketWsFrameDecoder;
import com.blt.talk.common.code.PacketWsFrameEncoder;
import com.blt.talk.message.server.handler.MessageWsServerHandler;
import com.blt.talk.message.server.manager.HandlerManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component("NettyWsServerInitializer")
public class NettyWebServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private HandlerManager handlerManager;
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        logger.info("******", pipeline.toString());        
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new WebSocketFrameAggregator(65536)); // Http消息组装  
        pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持  
         pipeline.addLast("decoder", new PacketWsFrameDecoder());
         pipeline.addLast("encoder", new PacketWsFrameEncoder());
        // pipeline.addLast("handler", new MessageSocketServerHandler(handlerManager));
        pipeline.addLast("handler", new MessageWsServerHandler(handlerManager));
    }

}
