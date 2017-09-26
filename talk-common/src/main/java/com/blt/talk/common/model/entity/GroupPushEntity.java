/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.model.entity;

import java.util.List;

import com.blt.talk.common.constant.DBConstant;

/**
 * 群推送信息
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
public class GroupPushEntity extends PeerEntity {

    private int groupType;
    private long creatorId;
    private int userCnt;
    /** Not-null value. */
    private List<ShieldStatusEntity> userList;
    private int version;
    private int status;

    public GroupPushEntity() {
    }

    public GroupPushEntity(Long id) {
        this.id = id;
    }

    public GroupPushEntity(Long id, int peerId, int groupType, String mainName, String avatar, int creatorId, int userCnt, int version, int status, int created, int updated) {
        this.id = id;
        this.peerId = peerId;
        this.groupType = groupType;
        this.mainName = mainName;
        this.avatar = avatar;
        this.creatorId = creatorId;
        this.userCnt = userCnt;
        this.version = version;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    /** Not-null value. */
    public String getMainName() {
        return mainName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    /** Not-null value. */
    public String getAvatar() {
        return avatar;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public int getUserCnt() {
        return userCnt;
    }

    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }

    public List<ShieldStatusEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<ShieldStatusEntity> userList) {
        this.userList = userList;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    // KEEP METHODS - put your custom methods here


    @Override
    public int getType() {
        return DBConstant.SESSION_TYPE_GROUP;
    }

}