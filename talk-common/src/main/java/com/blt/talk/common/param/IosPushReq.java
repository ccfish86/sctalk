/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

import java.util.List;

/**
 * 推送相关参数
 * 
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
public class IosPushReq {
    
    private String content;
    
    private int msgType;
    
    private Long fromId;
    
    private Long groupId;
    
    private List<UserToken> userTokenList;
    
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }


    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the msgType
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the fromId
     */
    public Long getFromId() {
        return fromId;
    }

    /**
     * @param fromId the fromId to set
     */
    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the userTokenList
     */
    public List<UserToken> getUserTokenList() {
        return userTokenList;
    }

    /**
     * @param userTokenList the userTokenList to set
     */
    public void setUserTokenList(List<UserToken> userTokenList) {
        this.userTokenList = userTokenList;
    }

}
