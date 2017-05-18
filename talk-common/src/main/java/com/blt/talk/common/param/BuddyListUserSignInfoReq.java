/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class BuddyListUserSignInfoReq {
    
    private long userId;
    private String signInfo;

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
     * @return the signInfo
     */
    public String getSignInfo() {
        return signInfo;
    }

    /**
     * @param signInfo the signInfo to set
     */
    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }
}
