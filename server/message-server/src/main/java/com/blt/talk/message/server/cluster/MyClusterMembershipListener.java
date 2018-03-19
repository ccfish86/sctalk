/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

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
        String uuid = membershipEvent.getMember().getUuid();
        logger.info("建立连接: {}",  uuid);
    }

    /* (non-Javadoc)
     * @see com.hazelcast.core.MembershipListener#memberRemoved(com.hazelcast.core.MembershipEvent)
     */
    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        String uuid = membershipEvent.getMember().getUuid();
        logger.info("关闭连接: {}", uuid);
        messageServerManager.unload(uuid);
        // userClientInfoManager.unloadServer(uuid);
    }

    /* (non-Javadoc)
     * @see com.hazelcast.core.MembershipListener#memberAttributeChanged(com.hazelcast.core.MemberAttributeEvent)
     */
    @Override
    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
        // TODO Auto-generated method stub

    }

}
