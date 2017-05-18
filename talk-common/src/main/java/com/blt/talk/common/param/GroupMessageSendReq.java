/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

import com.blt.talk.common.code.proto.IMBaseDefine;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class GroupMessageSendReq {

    private long userId;
    private long groupId;
    private IMBaseDefine.MsgType msgType;
    private int createTime;
    private String msgContent;
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
     * @return the groupId
     */
    public long getGroupId() {
        return groupId;
    }
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    /**
     * @return the msgType
     */
    public IMBaseDefine.MsgType getMsgType() {
        return msgType;
    }
    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(IMBaseDefine.MsgType msgType) {
        this.msgType = msgType;
    }
    /**
     * @return the createTime
     */
    public int getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
    /**
     * @return the msgContent
     */
    public String getMsgContent() {
        return msgContent;
    }
    /**
     * @param msgContent the msgContent to set
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    
}
