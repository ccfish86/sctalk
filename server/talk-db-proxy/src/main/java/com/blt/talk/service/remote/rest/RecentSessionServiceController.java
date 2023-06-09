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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.code.proto.IMBaseDefine.SessionType;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.ContactSessionEntity;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.service.internal.MessageService;
import com.blt.talk.service.jpa.entity.IMGroupMessageEntity;
import com.blt.talk.service.jpa.entity.IMMessageEntity;
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
    @Autowired
    private MessageService messageService;

    /**
     * 查询最新会话
     * @param userId 用户ID
     * @param lastUpdateTime 最后更新时间
     * @return 会话列表
     * @since  1.0
     */
    @GetMapping(path = "/recentSession")
    @Transactional
    public BaseModel<List<ContactSessionEntity>> getRecentSession(@RequestParam("userId") long userId,
            @RequestParam("updateTime") int lastUpdateTime) {

        SearchCriteria<IMRecentSession> recentSessionCriteria = new SearchCriteria<>();
        recentSessionCriteria.add(JpaRestrictions.eq("userId", userId, false));
        recentSessionCriteria.add(JpaRestrictions.eq("status", 0, false));
        recentSessionCriteria.add(JpaRestrictions.gt("updated", lastUpdateTime, false));

        // 取最多100条
        Sort sort = Sort.by(Sort.Direction.DESC, "updated");
        Pageable pageParam = PageRequest.of(0, 100, sort);
        Page<IMRecentSession> recentSessions = recentSessionRepository.findAll(recentSessionCriteria, pageParam);

        BaseModel<List<ContactSessionEntity>> sessionRes = new BaseModel<>();

        List<ContactSessionEntity> recentInfoList = new ArrayList<>();
        if (recentSessions.getSize() > 0) {
            recentSessions.forEach(recentSession -> {

                ContactSessionEntity recentInfo = new ContactSessionEntity();
                recentInfo.setId(recentSession.getId());
                recentInfo.setPeerId(recentSession.getPeerId());
                recentInfo.setPeerType(recentSession.getType());
                recentInfo.setCreated(recentSession.getCreated());
                recentInfo.setUpdated(recentSession.getUpdated());
                // fillSessionMsg
                if (recentSession.getType() == SessionType.SESSION_TYPE_GROUP_VALUE) {
                    // 群/讨论组
                    IMGroupMessageEntity latestMessage = messageService.getLatestGroupMessage(recentSession.getPeerId());
                    if (latestMessage != null) {
                        // 避免空消息 导致前端崩溃
                        recentInfo.setLatestMsgFromUserId(latestMessage.getUserId());
                        recentInfo.setLatestMsgId(latestMessage.getMsgId());
                        recentInfo.setLatestMsgType(latestMessage.getType());
                        recentInfo.setLatestMsgData(latestMessage.getContent());
                        recentInfoList.add(recentInfo);
                    }
                } else {
                    // 普通
                    IMMessageEntity  latestMessage = messageService.getLatestMessage(userId, recentSession.getPeerId());
                    if (latestMessage != null) {
                        // 避免空消息 导致前端崩溃
                        recentInfo.setLatestMsgFromUserId(latestMessage.getUserId());
                        recentInfo.setLatestMsgId(latestMessage.getMsgId());
                        recentInfo.setLatestMsgType(latestMessage.getType());
                        recentInfo.setLatestMsgData(latestMessage.getContent());
                        recentInfoList.add(recentInfo);
                    }
                }
            });
        }
        sessionRes.setData(recentInfoList);
        return sessionRes;
    }

}
