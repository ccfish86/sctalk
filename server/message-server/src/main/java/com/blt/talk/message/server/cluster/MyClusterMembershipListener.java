/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class MyClusterMembershipListener implements MembershipListener {

    @Autowired
    private MessageServerManager messageServerManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /* (non-Javadoc)
     * @see com.hazelcast.core.MembershipListener#memberAdded(com.hazelcast.core.MembershipEvent)
     */
    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        UUID uuid = membershipEvent.getMember().getUuid();
        logger.info("建立连接: {}",  uuid);
    }

    /* (non-Javadoc)
     * @see com.hazelcast.core.MembershipListener#memberRemoved(com.hazelcast.core.MembershipEvent)
     */
    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        UUID uuid = membershipEvent.getMember().getUuid();
        logger.info("关闭连接: {}", uuid);
        messageServerManager.unload(uuid.toString());
        // userClientInfoManager.unloadServer(uuid);
    }

}
