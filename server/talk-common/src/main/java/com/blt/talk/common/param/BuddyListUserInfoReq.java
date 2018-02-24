/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.param;

/**
 * 用户信息
 * @author 袁贵
 * @version 1.1
 * @since 1.1
 */
public class BuddyListUserInfoReq {
    
    private long userId;
    private String email;
    private String telephone;
    private String signInfo;
    private String avatarUrl;

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
     * @return the signInfo
     */
    public String getSignInfo() {
        return signInfo;
    }

    /**
     * @param signInfo the signInfo to set
     */
    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
}
