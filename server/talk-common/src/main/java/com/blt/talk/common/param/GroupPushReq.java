/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 设置群消息推送
 * <br>
 * 免打扰用
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class GroupPushReq {

    /** 用户ID */
    private long userId;
    /** 群ID */
    private long groupId;
    
    private int shieldStatus;

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
     * @return the groupId
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
    
}
