/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.model.entity;

/**
 * 推送设置
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class ShieldStatusEntity {

    private long userId;
    
    private int shieldStatus;

    private String userToken;
    
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
     * @return the shieldStatus
     */
    public int getShieldStatus() {
        return shieldStatus;
    }

    /**
     * @param shieldStatus the shieldStatus to set
     */
    public void setShieldStatus(int shieldStatus) {
        this.shieldStatus = shieldStatus;
    }

    /**
     * @return the userToken
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * @param userToken the userToken to set
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}
