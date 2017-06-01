/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.SessionEntity;

/**
 * 会话业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface RecentSessionService {
    /**
     * 查询最新会话
     * @param userId 用户ID
     * @param lastUpdateTime 最后更新时间
     * @return 会话列表
     * @since  1.0
     */
    BaseModel<List<SessionEntity>> getRecentSession(long userId, int lastUpdateTime);
}
