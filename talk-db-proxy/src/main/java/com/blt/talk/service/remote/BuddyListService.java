/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface BuddyListService {
    BaseModel<?> updateUserSignInfo(BuddyListUserSignInfoReq signInfo);
    BaseModel<List<UserEntity>> getAllUser(long userId, int lastUpdateTime);
    BaseModel<List<UserEntity>> getUserInfoList(List<Long> userIdListList);
}
