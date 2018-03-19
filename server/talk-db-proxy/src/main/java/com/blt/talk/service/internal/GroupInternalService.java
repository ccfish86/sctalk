/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

import java.util.List;

import com.blt.talk.common.model.entity.ShieldStatusEntity;

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
    
    /**
     * 群是否存在
     * <br>
     * 判断在Redis里是否有群相关数据
     * 
     * @param groupId 群ID
     * @return 是否在Redis里存在
     * @since  1.0
     */
    boolean isValidate(long groupId);

    /**
     * 用户是否在群里存在
     * <br>
     * 判断在Redis里是否有用户（群）相关数据
     * 
     * @param groupId 群ID
     * @param userId 用户ID
     * @return 是否在Redis里存在
     * @since  1.0
     */
    boolean isValidate(long groupId, long userId);
    
    /**
     * 查询屏蔽状态
     * 
     * @param groupId 群ID
     * @param userIdList 用户ID列表
     * @return 用户屏蔽状态
     * @since  1.0
     */
    List<ShieldStatusEntity> getGroupPush(long groupId, List<String> userIdList);
    
    /**
     * 设置群组信息推送，屏蔽或者取消屏蔽
     * 
     * @param groupId 群ID
     * @param userId 用户ID
     * @param shieldStatus 是否屏蔽
     * @since  1.0
     */
    void setGroupPush(long groupId, long userId, int shieldStatus);
}
