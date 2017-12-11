/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.message.server.cluster.MessageServerManager;
import com.blt.talk.message.server.model.LoginResponse;

/**
 * 提供登录服务
 * 
 * @author 袁贵
 * @version 1.0
 * @since  3.0
 */
@RestController
public class TalkLoginController {

    @Autowired
    private MessageServerManager messageServerManager;

    /**
     * 服务器列表
     * <br>
     * 用于手动确认服务器状态
     * 
     * @return
     * @since  1.0
     */
    @RequestMapping(value = "/", method={RequestMethod.GET})
    public BaseModel<List<String>> getServers() {
        List<String> servers = messageServerManager.allServerNames();
        BaseModel<List<String>> model = new BaseModel<>();
        model.setData(servers);
        return model;
    }
    
    /**
     * 登录
     * <br>
     * 用于分配消息服务器
     * @return 消息服务器
     * @since  1.0
     */
    @RequestMapping(value = "/msg_server", method={RequestMethod.GET})
    public BaseModel<LoginResponse> login() {

        MessageServerManager.MessageServerInfo server = messageServerManager.getUsableServer();
        
        BaseModel<LoginResponse> model = new BaseModel<>();
        if (server == null) {
            model.setCode(1);
            model.setMsg("没有服务");
        } else {
            LoginResponse data = new LoginResponse();
            data.setPriorIP(server.getIp());
            data.setBackupIP(server.getIp());
            
            //TODO 文件服务器地址（后续从配置文件读取地址）
            data.setMsfsPrior("http://192.168.10.74:8081");
            data.setMsfsBackup("192.168.10.48");
            
            data.setPort(server.getPort());
            model.setData(data);
        }
        
        return model;
    }
}
