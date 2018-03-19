/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.sctalk.turn;

import java.net.InetAddress;

import javax.annotation.PreDestroy;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.jitsi.turnserver.stack.TurnServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * turn server 启动类
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class ApplicationStarter implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);
    
    private TurnServer server;

    @PreDestroy
    public void destroy() {
        if (server != null && server.isStarted()) {
            server.shutDown();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {

        TransportAddress localAddress = null;
        if (args.length == 2) {
            localAddress = new TransportAddress(args[0], Integer.valueOf(args[1]), Transport.UDP);
        } else {
            InetAddress inad = InetAddress.getLocalHost();

            logger.info("turn server start at address {}", inad);
            localAddress = new TransportAddress(inad, 3478, Transport.UDP);
        }
        server = new TurnServer(localAddress);
        server.start();
        // Thread.sleep(600 * 1000);
        // if (server.isStarted())
        // {
        // server.shutDown();
        // }

    }

}
