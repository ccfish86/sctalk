/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.concurrent.Callable;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.param.IosPushReq;

/**
 * IOS推送相关处理
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
@FeignClient("talk-db-server")
public interface IphonePushService {

    /**
     * 推送到用户
     * @param pushReq 消息
     * @return 推送结果
     * @since  3.0
     */
    @PostMapping(value = "/push/toUsers")
    Callable<BaseModel<?>> sendToUsers(@RequestBody IosPushReq pushReq);
    
    /**
     * 用户device token取得
     * @param userId
     * @return
     * @since  3.0
     */
    @GetMapping(value = "/push/deviceToken")
    BaseModel<String> userDeviceToken(@RequestParam("userId") long userId);
    
}
