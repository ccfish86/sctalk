/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.BaseUnwrappedModel;
import com.blt.talk.common.param.RegistReq;
import com.blt.talk.message.server.cluster.MessageServerManager;
import com.blt.talk.message.server.model.LoginResponse;
import com.blt.talk.message.server.model.TalkServerResponse;
import com.blt.talk.message.server.remote.LoginService;

/**
 * 提供登录服务
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
public class TalkLoginController {

    @Autowired
    private MessageServerManager messageServerManager;
    @Autowired
    private LoginService loginService;
    
    private Logger logger =LoggerFactory.getLogger(getClass());
    
    /**
     * 服务器列表 <br>
     * 用于手动确认服务器状态
     * 
     * @return
     * @since 1.0
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public BaseUnwrappedModel<List<TalkServerResponse>> getServers() {
        List<String> serverIds = messageServerManager.allServerIds();
        List<TalkServerResponse> servers = new ArrayList<>();
        
        for (String uuid : serverIds) {
            MessageServerManager.MessageServerInfo server = messageServerManager.getServer(uuid);
            if (server != null) {
                
                Collection<Long> users = messageServerManager.getUserList(uuid);
                TalkServerResponse talkServerResponse = new TalkServerResponse();
                talkServerResponse.setServer(server.getIp());
                talkServerResponse.setUuid(uuid);
                talkServerResponse.setUserCount(users.size());
                talkServerResponse.setUsers(users);
                servers.add(talkServerResponse);
            }
        }
        
        BaseUnwrappedModel<List<TalkServerResponse>> model = new BaseUnwrappedModel<>();
        model.setData(servers);
        return model;
    }
    
    @PostMapping(value = "/regist")
    public BaseUnwrappedModel<?> regist(@RequestBody RegistReq req) {
        BaseModel<?> res = loginService.regist(req);
        BaseUnwrappedModel<?> unwrappedModel = new BaseUnwrappedModel<>(res);
        return unwrappedModel;
    }

    /**
     * 登录 <br>
     * 用于分配消息服务器
     * 
     * @return 消息服务器
     * @since 1.0
     */
    @RequestMapping(value = "/msg_server", method = {RequestMethod.GET})
    public BaseUnwrappedModel<LoginResponse> login(HttpServletRequest request) {
        
        List<String> servers = messageServerManager.allServerIds();

        // MessageServerManager.MessageServerInfo
        BaseUnwrappedModel<LoginResponse> model = new BaseUnwrappedModel<>();
        if (servers == null || servers.isEmpty()) {
            model.setCode(1);
            model.setMsg("没有服务");
        } else {
            LoginResponse data = new LoginResponse();
            
            // 处理负载
            String freeUuid = null;
            int minUserCount = Integer.MAX_VALUE;
            for (String uuid : servers) {
                int connectionCount = messageServerManager.getConnectCount(uuid);
                if (connectionCount == 0) {
                    freeUuid = uuid;
                    break;
                } else {
                    if (minUserCount > connectionCount) {
                        minUserCount = connectionCount;
                        freeUuid = uuid;
                    }
                }
            }
            MessageServerManager.MessageServerInfo server = messageServerManager.getServer(freeUuid);
            
            data.setPriorIP(server.getPriorIP());
            data.setBackupIP(server.getIp());
            
            // 文件服务器地址（后续从配置文件读取地址）
            String contextpath;
            contextpath = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();

            data.setMsfsPrior(
                    contextpath.endsWith("/") ? contextpath + "upload" : contextpath + "/upload");
            data.setMsfsBackup("192.168.10.48");

            data.setPort(server.getPort());
            model.setData(data);
        }

        return model;
    }
}
