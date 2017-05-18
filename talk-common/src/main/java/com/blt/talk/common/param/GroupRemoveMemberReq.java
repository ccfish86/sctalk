/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

import java.util.List;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class GroupRemoveMemberReq {

    private long groupId;

    private List<Integer> userIds;

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
     * @return the userIds
     */
    public List<Integer> getUserIds() {
        return userIds;
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }


}
