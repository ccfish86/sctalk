/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.KickUserReq;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.param.RegistReq;
import com.blt.talk.common.param.UserToken;

/**
 * 登录业务远程调用Service
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */

@FeignClient("talk-db-server")
public interface LoginService {

    @PostMapping(path = "/login")
    BaseModel<UserEntity> login(@RequestBody LoginReq loginReq);
    
    @PostMapping(path = "/regist")
    public BaseModel<Long> regist(@RequestBody RegistReq param);
    /**
     * 
     * @param userToken 用户Tken
     * @return 
     * @since  1.0
     */
    @PostMapping(path = "/login/deviceToken")
    BaseModel<Long> setDeviceToken(@RequestBody UserToken userToken);
    
    @PostMapping(path = "/login/kickPcClient")
    BaseModel<Long> kickPcClient(@RequestBody KickUserReq kickUserReq );//route
    
    @PostMapping(path = "/login/pushShield")
    BaseModel<Integer> pushShield(@RequestParam("userId") long userId, @RequestParam("shieldStatus") int shieldStatus);
    
    @GetMapping(path = "/login/queryPushShield")
    BaseModel<Integer> queryPushShield(@RequestParam("userId") long userId);
    
}
