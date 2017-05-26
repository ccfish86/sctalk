/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

import java.util.List;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface GroupInternalService {

    /**
     * 追加新成员，并显示最新的用户
     * @param userId
     * @param groupId
     * @param members
     * @return
     * @since  1.0
     */
    List<Long> insertNewMember(long userId, long groupId, List<Long> members);

    /**
     * 删除成员，并显示最新的用户
     * @param userId
     * @param groupId
     * @param members
     * @return
     * @since  1.0
     */
    List<Long> removeMember(long userId, long groupId, List<Long> members);
    
    boolean isValidate(long groupId);
}
