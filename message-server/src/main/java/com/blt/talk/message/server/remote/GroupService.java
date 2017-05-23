/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupInsertNewMemberReq;
import com.blt.talk.common.param.GroupRemoveMemberReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient("talk-db-server")
public interface GroupService {

    /**
     * 查询组属性
     * @param userId
     * @return
     * @since  1.0
     */
    @RequestMapping(method = RequestMethod.GET, value = "/group/normalList")
    BaseModel<List<GroupEntity>> normalList(@RequestParam("userId") long userId);
    
    /**
     * 追加新成员，并显示最新的用户
     * @param newMemberReq
     * @return
     * @since  1.0
     */
    @RequestMapping(method = RequestMethod.POST, value = "/group/insertNewMember")
    BaseModel<List<Integer>> insertNewMember(@RequestBody GroupInsertNewMemberReq newMemberReq);

    /**
     * 删除成员，并显示最新的用户
     * @param newMemberReq
     * @return
     * @since  1.0
     */
    @RequestMapping(method = RequestMethod.POST, value = "/group/removeMember")
    BaseModel<List<Integer>> removeMember(@RequestBody GroupRemoveMemberReq newMemberReq);
}
