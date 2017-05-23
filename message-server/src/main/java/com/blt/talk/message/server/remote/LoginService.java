/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */

@FeignClient("talk-db-server")
public interface LoginService {

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    BaseModel<UserEntity> login(@RequestBody LoginReq loginReq);
}
