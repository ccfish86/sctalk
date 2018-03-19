/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class BuddyListUserAvatarReq {

    private long userId;
    private String avatarUrl;
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
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }
    /**
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
}
