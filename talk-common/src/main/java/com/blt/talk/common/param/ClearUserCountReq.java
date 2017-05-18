/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

import com.blt.talk.common.code.proto.IMBaseDefine;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClearUserCountReq {

    private long userId;
    private long peerId;
    private IMBaseDefine.SessionType sessionType;
    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return the peerId
     */
    public long getPeerId() {
        return peerId;
    }
    /**
     * @param peerId the peerId to set
     */
    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }
    /**
     * @return the sessionType
     */
    public IMBaseDefine.SessionType getSessionType() {
        return sessionType;
    }
    /**
     * @param sessionType the sessionType to set
     */
    public void setSessionType(IMBaseDefine.SessionType sessionType) {
        this.sessionType = sessionType;
    }
    
}
