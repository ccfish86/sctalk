/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
public class IndexController {

    @Value("${spring.application.name}")
    String appName;
    
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public BaseModel<String> index() {
        BaseModel<String> response = new BaseModel<>();
        response.setData(appName);
        return response;
    }
}
