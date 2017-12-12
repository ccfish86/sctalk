/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.message.server.cluster.MessageServerManager;
import com.blt.talk.message.server.model.LoginResponse;
import com.blt.talk.message.server.model.TalkServerResponse;
import com.blt.talk.message.server.runnable.QueryUserListTask;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

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
    private HazelcastInstance hazelcastInstance;
    
    private Logger logger =LoggerFactory.getLogger(getClass());
    
    /**
     * 服务器列表 <br>
     * 用于手动确认服务器状态
     * 
     * @return
     * @since 1.0
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public BaseModel<List<TalkServerResponse>> getServers() {
        List<TalkServerResponse> servers = messageServerManager.allServers();
        BaseModel<List<TalkServerResponse>> model = new BaseModel<>();
        model.setData(servers);
        return model;
    }

    /**
     * 更新在线用户数
     * @param request
     * @return
     * @since  1.0
     */
    @RequestMapping(value = "/update", method = {RequestMethod.GET})
    public BaseModel<Map<String, Integer>> updata(HttpServletRequest request) {
        Map<Member, Future<List<Long>>> members = hazelcastInstance.getExecutorService("default").submitToAllMembers(new QueryUserListTask());
        BaseModel<Map<String, Integer>> result = new BaseModel<>();
        try {
            
            String uuid = "";
            Map<String, Integer> map = new HashMap<>();
            
            for (Entry<Member, Future<List<Long>>> set:members.entrySet()) {
                Future<List<Long>> userListFuture = set.getValue();
                List<Long> userList = userListFuture.get();
                if (!userList.isEmpty()) {
                    uuid = set.getKey().getUuid();
                    logger.debug("Update user count{},{}", uuid, userList.size());
                    messageServerManager.update(uuid, userList.size());
                    map.put(uuid, userList.size());
                }
            }
            result.setData(map);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 登录 <br>
     * 用于分配消息服务器
     * 
     * @return 消息服务器
     * @since 1.0
     */
    @RequestMapping(value = "/msg_server", method = {RequestMethod.GET})
    public BaseModel<LoginResponse> login(HttpServletRequest request) {
        
        MessageServerManager.MessageServerInfo server = messageServerManager.getUsableServer();

        BaseModel<LoginResponse> model = new BaseModel<>();
        if (server == null) {
            model.setCode(1);
            model.setMsg("没有服务");
        } else {
            LoginResponse data = new LoginResponse();
            data.setPriorIP(server.getIp());
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
