/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class SessionUpdateReq {

    private long sessionId;
    
    private int updateTime;
    /**
     * @return the sessionId
     */
    public long getSessionId() {
        return sessionId;
    }
    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(final long sessionId) {
        this.sessionId = sessionId;
    }
    /**
     * @return the updateTime
     */
    public int getUpdateTime() {
        return updateTime;
    }
    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }
    
}
