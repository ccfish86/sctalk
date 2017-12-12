/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Client;
import com.hazelcast.core.ClientListener;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class MyClusterClientListener implements ClientListener {

    @Autowired
    private MessageServerManager messageServerManager;

    @Override
    public void clientConnected(Client client) {
        // do nth.
    }

    @Override
    public void clientDisconnected(Client client) {
        // 清除节点信息
        messageServerManager.unload(client.getUuid());
    }

}
