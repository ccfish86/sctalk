/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.model.entity;

/**
 * 联系人信息
 * 
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
public class ContactSessionEntity {
    private Long id;
    private String sessionKey;
    private long peerId;
    private int peerType;
    private long latestMsgFromUserId;
    private int latestMsgType;
    private long latestMsgId;
    private String latestMsgData;
    private int created;
    private int updated;
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the sessionKey
     */
    public String getSessionKey() {
        return sessionKey;
    }
    /**
     * @param sessionKey the sessionKey to set
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
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
     * @return the peerType
     */
    public int getPeerType() {
        return peerType;
    }
    /**
     * @param peerType the peerType to set
     */
    public void setPeerType(int peerType) {
        this.peerType = peerType;
    }
    /**
     * @return the latestMsgType
     */
    public int getLatestMsgType() {
        return latestMsgType;
    }
    /**
     * @param latestMsgType the latestMsgType to set
     */
    public void setLatestMsgType(int latestMsgType) {
        this.latestMsgType = latestMsgType;
    }
    /**
     * @return the latestMsgId
     */
    public long getLatestMsgId() {
        return latestMsgId;
    }
    /**
     * @param latestMsgId the latestMsgId to set
     */
    public void setLatestMsgId(long latestMsgId) {
        this.latestMsgId = latestMsgId;
    }
    /**
     * @return the latestMsgData
     */
    public String getLatestMsgData() {
        return latestMsgData;
    }
    /**
     * @param latestMsgData the latestMsgData to set
     */
    public void setLatestMsgData(String latestMsgData) {
        this.latestMsgData = latestMsgData;
    }
    /**
     * @return the latestMsgFromUserId
     */
    public long getLatestMsgFromUserId() {
        return latestMsgFromUserId;
    }
    /**
     * @param latestMsgFromUserId the latestMsgFromUserId to set
     */
    public void setLatestMsgFromUserId(long latestMsgFromUserId) {
        this.latestMsgFromUserId = latestMsgFromUserId;
    }
    /**
     * @return the created
     */
    public int getCreated() {
        return created;
    }
    /**
     * @param created the created to set
     */
    public void setCreated(int created) {
        this.created = created;
    }
    /**
     * @return the updated
     */
    public int getUpdated() {
        return updated;
    }
    /**
     * @param updated the updated to set
     */
    public void setUpdated(int updated) {
        this.updated = updated;
    }
    
    
}
