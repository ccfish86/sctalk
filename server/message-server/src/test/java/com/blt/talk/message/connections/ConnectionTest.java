/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.connections;

import java.util.concurrent.CountDownLatch;

import com.blt.talk.common.code.PacketEncoder;
import com.blt.talk.message.client.ClientMessageClientHandler;
import com.blt.talk.message.client.ClientPacketDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 客户端测试
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class ConnectionTest {

    private static CountDownLatch coutDown = new CountDownLatch(1000);

    /**
     * @param args
     * @since 1.0
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for (int i = 100; i < 1100; i++) {
            TTlogin tl = new TTlogin(String.valueOf(i));
            new Thread(tl).start();
        }

        try {
            coutDown.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static class TTlogin implements Runnable {
        String name;

        TTlogin(String name) {
            this.name = name;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {

            // 连接TT服务器
            Bootstrap bootStrap = new Bootstrap();

            NioEventLoopGroup group = new NioEventLoopGroup();
            bootStrap.group(group).channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer(name));

            bootStrap.connect("tt message server's ip", 8801).channel();
            coutDown.countDown();
        }

    }

    static class SimpleChatClientInitializer extends ChannelInitializer<SocketChannel> {

        private String name;

        public SimpleChatClientInitializer(String name) {
            this.name = name;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(400 * 1024, 0, 4, -4, 0));
            pipeline.addLast("decoder", new ClientPacketDecoder());
            pipeline.addLast("encoder", new PacketEncoder());
            pipeline.addLast(new LoggingHandler(LogLevel.WARN));
            pipeline.addLast("handler", new ClientMessageClientHandler(name));
        }
    }
}
