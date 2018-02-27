/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 删除会话请求
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
public class SessionRemoveReq {

    private long userId;
    private long peerId;
    private int type;
    
    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return the peerId
     */
    public long getPeerId() {
        return peerId;
    }
    /**
     * @param peerId the peerId to set
     */
    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}
