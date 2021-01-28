/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserAvatarReq;
import com.blt.talk.common.param.BuddyListUserInfoReq;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;
import com.blt.talk.common.param.SessionRemoveReq;

/**
 * 通讯录业务远程调用Service
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient("talk-db-server")
public interface BuddyListService {

    @PostMapping(path = "/buddyList/updateUserSignInfo")
    BaseModel<?> updateUserSignInfo(@RequestBody BuddyListUserSignInfoReq signInfoReq);
    
    @GetMapping(path = "/buddyList/allUser")
    BaseModel<List<UserEntity>> getAllUser(@RequestParam("userId") long userId, @RequestParam("updateTime") int lastUpdateTime);

    @GetMapping(path = "/buddyList/userInfo")
    BaseModel<List<UserEntity>> getUserInfoList(@RequestParam("userId") List<Long> userIdListList);

    @PostMapping(path = "/buddyList/removeSession")
    BaseModel<?> removeSession(SessionRemoveReq sessionRemoveReq);

    @PostMapping(path = "/buddyList/changeAvatar")
    BaseModel<?> updateUserAvatar(@RequestBody BuddyListUserAvatarReq userAvatarReq);

    @GetMapping(path = "/buddyList/changeUserInfo")
    BaseModel<?> updateUserInfo(BuddyListUserInfoReq userInfoReq);

}
