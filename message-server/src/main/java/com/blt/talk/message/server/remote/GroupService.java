/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupUpdateMemberReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient("talk-db-server")
public interface GroupService {

    /**
     * 查询组Version属性
     * @param userId
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/group/normalList")
    BaseModel<List<GroupEntity>> normalList(@RequestParam("userId") long userId);
    
    // 合并至【更改群员】
//    /**
//     * 追加新成员，并显示最新的用户
//     * @param newMemberReq
//     * @return
//     * @since  1.0
//     */
//    @PostMapping(path = "/group/insertNewMember")
//    BaseModel<List<Integer>> insertNewMember(@RequestBody GroupUpdateMemberReq newMemberReq);
//
//    /**
//     * 删除成员，并显示最新的用户
//     * @param newMemberReq
//     * @return
//     * @since  1.0
//     */
//    @PostMapping(path = "/group/removeMember")
//    BaseModel<List<Integer>> removeMember(@RequestBody GroupUpdateMemberReq newMemberReq);

    /**
     * 查询组的属性
     * @param groupVersionList
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/group/groupInfoList")
    BaseModel<List<GroupEntity>> groupInfoList(@RequestParam("groupIdList") List<Long> groupIdList);
    
    /**
     * 查询组的属性
     * @param groupVersionList
     * @return
     * @since  1.0
     */
    @PostMapping(path = "/group/infoList")
    BaseModel<List<GroupEntity>> groupInfoList(@RequestBody Map<String, Integer> groupIdList);
    
    /**
     * 创建群组
     * @param groupEntity 群资料
     * @return 创建结果:新群的ID
     * @since  1.0
     */
    @PostMapping(path = "/group/createGroup")
    BaseModel<Long> createGroup(@RequestBody GroupEntity groupEntity);
    
    /**
     * 更改群员
     * @param groupMember 群员信息
     * @return 创建结果:群的现有成员列表
     * @since  1.0
     */
    @PostMapping(path = "/group/updateMember")
    BaseModel<List<Long>> changeGroupMember(@RequestBody GroupUpdateMemberReq groupMember);

    /**
     * @param userId
     * @return
     * @since  1.0
     */
    @PostMapping(path = "/group/groupShieldStatus")
    BaseModel<Integer> groupShieldStatus(long userId);
}
