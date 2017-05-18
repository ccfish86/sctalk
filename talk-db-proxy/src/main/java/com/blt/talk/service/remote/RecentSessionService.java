/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.common.param.SessionAddReq;
import com.blt.talk.common.param.SessionUpdateReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface RecentSessionService {
    BaseModel<List<SessionEntity>> getRecentSession(long userId, int lastUpdateTime);
    BaseModel<Long> getSessionId(long userId, long peerId, int type, boolean isAll);
    BaseModel<Long> addSession(SessionAddReq sessionReq);
    BaseModel<?> updateSession(SessionUpdateReq sessionReq);
    BaseModel<?> removeSession(long sessionId);
}
