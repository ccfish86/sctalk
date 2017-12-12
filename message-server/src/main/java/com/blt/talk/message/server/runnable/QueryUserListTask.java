/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.runnable;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

import com.blt.talk.message.server.manager.ClientUserManager;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class QueryUserListTask implements Callable<List<Long>>, Serializable {
    
    public List<Long> call() {
        List<Long> userList = ClientUserManager.getOnlineUserList();
        return userList;
    }
}
