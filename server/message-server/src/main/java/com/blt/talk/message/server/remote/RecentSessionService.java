/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.ContactSessionEntity;
import com.blt.talk.common.model.entity.SessionEntity;

/**
 * 获取最近的会话
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient("talk-db-server")
public interface RecentSessionService {

    @GetMapping(path = "/session/recentSession")
    BaseModel<List<ContactSessionEntity>> getRecentSession(@RequestParam("userId") long userId, @RequestParam("updateTime") int lastUpdateTime);

}
