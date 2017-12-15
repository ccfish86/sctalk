/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
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
    
    /**
     * 在线用户查询
     * <br>
     * 用于分配消息服务器
     * @return 消息服务器
     * @since  1.0
     */
    @RequestMapping(value = "/online/{id}", method={RequestMethod.GET})
    public BaseModel<UserClientInfoManager.UserClientInfo> online(@PathVariable("id") Long id) {
        
        UserClientInfoManager.UserClientInfo clientInfo = userClientInfoManager.getUserInfo(id);
        
        BaseModel<UserClientInfoManager.UserClientInfo> result = new BaseModel<>();
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
    public BaseModel<Map<Long, List<String>>> allOnline() {
        
        Collection<UserClientInfoManager.UserClientInfo> clientInfos = userClientInfoManager.allUsers();
        
        Map<Long, List<String>> userConnMapList = new HashMap<>();
        
        for(UserClientInfoManager.UserClientInfo user :clientInfos) {
            userConnMapList.put(user.getUserId(), user.getRouteConns());
        }
        
        BaseModel<Map<Long, List<String>>> result = new BaseModel<>();
        result.setData(userConnMapList);
        return result;
    }
}
