/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.common.param.SessionAddReq;
import com.blt.talk.common.param.SessionUpdateReq;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.jpa.entity.IMRecentSession;
import com.blt.talk.service.jpa.repository.IMRecentSessionRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.remote.RecentSessionService;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/session")
public class RecentSessionServiceController implements RecentSessionService {

    @Autowired
    private IMRecentSessionRepository recentSessionRepository;

    @Override
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
                recentInfo.setPeerType(recentSession.getType());
                recentInfo.setLatestMsgType(recentSession.getStatus());
                recentInfo.setCreated(recentSession.getCreated());
                recentInfo.setUpdated(recentSession.getUpdated());
                recentInfo.setPeerId(recentSession.getPeerId());
                recentInfoList.add(recentInfo);
            });
        }

        return sessionRes;
    }

//    @Override
//    @GetMapping(path = "/sessionId")
//    public BaseModel<Long> getSessionId(@RequestParam("userId") long userId, @RequestParam("peerId") long peerId,
//            @RequestParam("type") int type, @RequestParam("isAll") boolean isAll) {
//
//        byte status = 0;
//
//        // 查询已有数据
//        SearchCriteria<IMRecentSession> recentSessionCriteria = new SearchCriteria<>();
//        recentSessionCriteria.add(JpaRestrictions.eq("userId", userId, false));
//        recentSessionCriteria.add(JpaRestrictions.eq("peerId", peerId, false));
//        recentSessionCriteria.add(JpaRestrictions.eq("type", type, false));
//        if (!isAll) {
//            recentSessionCriteria.add(JpaRestrictions.eq("status", status, false));
//        }
//
//        List<IMRecentSession> recentSessions =
//                recentSessionRepository.findAll(recentSessionCriteria, new Sort(Sort.Direction.DESC, "updated"));
//
//        if (!recentSessions.isEmpty()) {
//            return new BaseModel<Long>() {
//                {
//                    setData(recentSessions.get(0).getId());
//                }
//            };
//        }
//        return new BaseModel<Long>();
//    }
//
//    @Override
//    @PostMapping(path = "/addSession")
//    public BaseModel<Long> addSession(@RequestBody SessionAddReq sessionReq) {
//
//        byte status = 0;
//        int time = CommonUtils.currentTimeSeconds();
//
//        // 查询已有数据
//        SearchCriteria<IMRecentSession> recentSessionCriteria = new SearchCriteria<>();
//        recentSessionCriteria.add(JpaRestrictions.eq("userId", sessionReq.getUserId(), false));
//        recentSessionCriteria.add(JpaRestrictions.eq("peerId", sessionReq.getPeerId(), false));
//        recentSessionCriteria.add(JpaRestrictions.eq("type", sessionReq.getType(), false));
//
//        List<IMRecentSession> recentSessions =
//                recentSessionRepository.findAll(recentSessionCriteria, new Sort(Sort.Direction.DESC, "updated"));
//
//        IMRecentSession recentSession;
//        if (!recentSessions.isEmpty()) {
//            recentSession = recentSessions.get(0);
//            recentSession.setStatus(status);
//            recentSession.setUpdated(time);
//        } else {
//
//            byte type = (byte) sessionReq.getType();
//            recentSession = new IMRecentSession();
//            recentSession.setUserId(sessionReq.getUserId());
//            recentSession.setPeerId(sessionReq.getPeerId());
//            recentSession.setType(type);
//            recentSession.setStatus(status);
//            recentSession.setCreated(time);
//            recentSession.setUpdated(time);
//        }
//
//        recentSession = recentSessionRepository.save(recentSession);
//
//        long sessionId = recentSession.getId();
//        return new BaseModel<Long>() {
//            {
//                setData(sessionId);
//            }
//        };
//    }
//
//    @Override
//    @PostMapping(path = "/updateSession")
//    public BaseModel<?> updateSession(@RequestBody SessionUpdateReq sessionReq) {
//
//        IMRecentSession session = recentSessionRepository.findOne(sessionReq.getSessionId());
//        session.setUpdated(sessionReq.getUpdateTime());
//
//        recentSessionRepository.save(session);
//
//        return new BaseModel<String>();
//    }
//
//    @Override
//    @DeleteMapping(path = "/removeSession")
//    public BaseModel<?> removeSession(@RequestParam("sessionId") long sessionId) {
//
//        byte status = 1;
//        int time = CommonUtils.currentTimeSeconds();
//        IMRecentSession session = recentSessionRepository.findOne(sessionId);
//        session.setStatus(status);
//        session.setUpdated(time);
//
//        recentSessionRepository.save(session);
//
//        return new BaseModel<String>();
//    }
}
