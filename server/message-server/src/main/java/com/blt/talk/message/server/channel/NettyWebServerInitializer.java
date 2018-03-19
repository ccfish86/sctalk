/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.message.server.manager.HandlerManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

/**
 * Websocket支持
 * <br>
 * 客户端测试通过：https://github.com/xiaominfc/teamtalk_websocket_client
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component("NettyWsServerInitializer")
public class NettyWebServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SslContext sslCtx;
    
    public NettyWebServerInitializer() {

        // Configure SSL context
        // TODO 证书 sslCtx = SslContext.newServerContext(certificate, privateKey);
        logger.warn("暂不支持SSL，如果需要，请配置对应的证书文件");
        sslCtx = null;
    }
    
    @Autowired
    private HandlerManager handlerManager;
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        PortUnificationServerHandler portUnificationServerHandler = new PortUnificationServerHandler(sslCtx);
        portUnificationServerHandler.setHandlerManager(handlerManager);
        pipeline.addLast(portUnificationServerHandler);
    }

}
