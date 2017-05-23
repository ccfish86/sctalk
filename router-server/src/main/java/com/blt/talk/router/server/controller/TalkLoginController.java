/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.router.server.manager.MessageServerManager;
import com.blt.talk.router.server.model.LoginResponse;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  3.0
 */
@RestController
public class TalkLoginController {

    @RequestMapping(value = "/msg_server", method={RequestMethod.GET})
    public BaseModel<LoginResponse> login() {

        MessageServerManager.MessageServerInfo server = MessageServerManager.getUsableServer();
        
        BaseModel<LoginResponse> model = new BaseModel<>();
        if (server == null) {
            model.setCode(1);
            model.setMsg("没有服务");
        } else {
            LoginResponse data = new LoginResponse();
            data.setPriorIP(server.getIp());
            data.setBackupIP(server.getIp());
            
            data.setMsfsPrior("192.168.10.48");
            data.setMsfsBackup("192.168.10.48");
            
            data.setPort(server.getPort());
            model.setData(data);
        }
        
        return model;
    }
    @RequestMapping(value = "/msg_server", method={RequestMethod.POST})
    public BaseModel<LoginResponse> postLogin(HttpServletRequest request) {
        
        try {
            String content = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
            System.out.println(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        BaseModel<LoginResponse> model = new BaseModel<>();
        
        return model;
    }
}
