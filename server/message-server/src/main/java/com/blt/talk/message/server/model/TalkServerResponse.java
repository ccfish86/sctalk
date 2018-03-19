/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * 服务器连接信息
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class TalkServerResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4901415930535565519L;

    private String server;
    
    private String uuid;

    private Integer userCount;

    private Collection<Long> users;

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the userCount
     */
    public Integer getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    /**
     * @return the users
     */
    public Collection<Long> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Collection<Long> users) {
        this.users = users;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
