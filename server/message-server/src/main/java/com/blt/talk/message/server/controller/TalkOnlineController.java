/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseUnwrappedModel;
import com.blt.talk.message.server.cluster.MessageServerManager;
import com.blt.talk.message.server.cluster.MessageServerManager.MessageServerInfo;
import com.blt.talk.message.server.cluster.UserClientInfoManager;

/**
 * 提供在线用户查询服务
 * <br>
 * 供测试时追踪用户在线状态
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@RestController
public class TalkOnlineController {

    @Autowired
    private UserClientInfoManager userClientInfoManager;
    @Autowired
    private MessageServerManager messageServerManager;
    
    /**
     * 在线用户查询
     * <br>
     * 用于分配消息服务器
     * @return 消息服务器
     * @since  1.0
     */
    @RequestMapping(value = "/online/{id}", method={RequestMethod.GET})
    public BaseUnwrappedModel<UserClientInfoManager.UserClientInfo> online(@PathVariable("id") Long id) {
        
        UserClientInfoManager.UserClientInfo clientInfo = userClientInfoManager.getUserInfo(id);
        
        BaseUnwrappedModel<UserClientInfoManager.UserClientInfo> result = new BaseUnwrappedModel<>();
        result.setData(clientInfo);
        return result;
    }

    /**
     * 在线用户查询
     * <br>
     * 用于分配消息服务器
     * @return 消息服务器
     * @since  1.0
     */
    @RequestMapping(value = "/onlines", method={RequestMethod.GET})
    public BaseUnwrappedModel<Map<Long, List<MessageServerInfo>>> allOnline() {
        
        Collection<UserClientInfoManager.UserClientInfo> clientInfos = userClientInfoManager.allUsers();
        
        Map<Long, List<MessageServerInfo>> userConnMapList = new HashMap<>();
        
        for(UserClientInfoManager.UserClientInfo user :clientInfos) {
            List<Long> conns = user.getRouteConns();
            List<MessageServerInfo> servers = new ArrayList<>();
            for (Long conn : conns) {
                MessageServerInfo server = messageServerManager.getServerBy(conn);
                if (server!= null) {
                    servers.add(server);
                }
            }
            userConnMapList.put(user.getUserId(), servers);
            
        }
        
        BaseUnwrappedModel<Map<Long, List<MessageServerInfo>>> result = new BaseUnwrappedModel<>();
        result.setData(userConnMapList);
        return result;
    }
}
