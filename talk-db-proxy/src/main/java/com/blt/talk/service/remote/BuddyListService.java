/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;

/**
 * 通信录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface BuddyListService {
    
    /**
     * 更新用户签名
     * @param signInfo 签名
     * @return 更新结果
     * @since  1.0
     */
    BaseModel<?> updateUserSignInfo(BuddyListUserSignInfoReq signInfo);
    /**
     * 查询所有用户
     * @param userId
     * @param lastUpdateTime
     * @return
     * @since  1.0
     */
    BaseModel<List<UserEntity>> getAllUser(long userId, int lastUpdateTime);
    /**
     * 查询用户信息
     * @param userIdListList
     * @return
     * @since  1.0
     */
    BaseModel<List<UserEntity>> getUserInfoList(List<Long> userIdListList);
}
