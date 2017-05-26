/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

import java.util.List;

import com.blt.talk.common.code.proto.IMBaseDefine;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class GroupUpdateMemberReq {

    private long userId;
    
    private IMBaseDefine.GroupModifyType updType;
    
    private long groupId;

    private List<Long> userIds;

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
     * @return the updType
     */
    public IMBaseDefine.GroupModifyType getUpdType() {
        return updType;
    }

    /**
     * @param updType the updType to set
     */
    public void setUpdType(IMBaseDefine.GroupModifyType updType) {
        this.updType = updType;
    }

    /**
     * @return the userIds
     */
    public List<Long> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }


}
