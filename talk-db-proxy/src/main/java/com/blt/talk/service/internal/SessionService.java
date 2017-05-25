/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface SessionService {

    long addSession(long userId, long peerId, int type);
    
    long getSessionId(long userId, long peerId, int type, boolean isAll);
    
    void remove(long sessionId);
    
    void update(long sessionId, int time);
}
