/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.service.jpa.entity.IMRecentSession;
import com.blt.talk.service.jpa.repository.IMRecentSessionRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;

/**
 * 会话业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/session")
public class RecentSessionServiceController {

    @Autowired
    private IMRecentSessionRepository recentSessionRepository;

    /**
     * 查询最新会话
     * @param userId 用户ID
     * @param lastUpdateTime 最后更新时间
     * @return 会话列表
     * @since  1.0
     */
    @GetMapping(path = "/recentSession")
    public BaseModel<List<SessionEntity>> getRecentSession(@RequestParam("userId") long userId,
            @RequestParam("updateTime") int lastUpdateTime) {

        SearchCriteria<IMRecentSession> recentSessionCriteria = new SearchCriteria<>();
        recentSessionCriteria.add(JpaRestrictions.eq("userId", userId, false));
        recentSessionCriteria.add(JpaRestrictions.eq("status", 0, false));
        recentSessionCriteria.add(JpaRestrictions.gt("updated", lastUpdateTime, false));

        // 取最多100条
        Sort sort = new Sort(Sort.Direction.DESC, "updated");
        Pageable pageParam = new PageRequest(0, 100, sort);
        Page<IMRecentSession> recentSessions = recentSessionRepository.findAll(recentSessionCriteria, pageParam);

        BaseModel<List<SessionEntity>> sessionRes = new BaseModel<List<SessionEntity>>();

        List<SessionEntity> recentInfoList = new ArrayList<>();
        if (recentSessions.getSize() > 0) {
            recentSessions.forEach(recentSession -> {

                SessionEntity recentInfo = new SessionEntity();
                recentInfo.setId(recentSession.getId());
                recentInfo.setPeerId(recentSession.getPeerId());
                recentInfo.setPeerType(recentSession.getType());
                recentInfo.setTalkId(recentSession.getUserId());
                recentInfo.setLatestMsgType(recentSession.getType());
                recentInfo.setCreated(recentSession.getCreated());
                recentInfo.setUpdated(recentSession.getUpdated());
                recentInfoList.add(recentInfo);
            });
        }
        sessionRes.setData(recentInfoList);
        return sessionRes;
    }

}
