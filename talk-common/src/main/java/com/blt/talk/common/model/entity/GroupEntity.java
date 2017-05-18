package com.blt.talk.common.model.entity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.blt.talk.common.constant.DBConstant;

/**
 * Entity mapped to table GroupInfo.
 */
public class GroupEntity extends PeerEntity {

    private int groupType;
    private long creatorId;
    private int userCnt;
    /** Not-null value. */
    private String userList;
    private int version;
    private int status;


    public GroupEntity() {
    }

    public GroupEntity(Long id) {
        this.id = id;
    }

    public GroupEntity(Long id, int peerId, int groupType, String mainName, String avatar, int creatorId, int userCnt, String userList, int version, int status, int created, int updated) {
        this.id = id;
        this.peerId = peerId;
        this.groupType = groupType;
        this.mainName = mainName;
        this.avatar = avatar;
        this.creatorId = creatorId;
        this.userCnt = userCnt;
        this.userList = userList;
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

    /** Not-null value. */
    public String getUserList() {
        return userList;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUserList(String userList) {
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

    /**
     * yingmu
     * 获取群组成员的list
     * -- userList 前后去空格，按照逗号区分， 不检测空的成员(非法)
     */
    public Set<Long> getlistGroupMemberIds() {
        if (userList == null || userList.length() == 0) {
            return Collections.emptySet();
        }
        String[] arrayUserIds = userList.trim().split(",");
        if (arrayUserIds.length <= 0) {
            return Collections.emptySet();
        }
        /** zhe'g */
        Set<Long> result = new TreeSet<Long>();
        for (int index = 0; index < arrayUserIds.length; index++) {
            long userId = Long.parseLong(arrayUserIds[index]);
            result.add(userId);
        }
        return result;
    }
    //todo 入参变为 set【自动去重】
    // 每次都要转换 性能不是太好，todo
    public void setlistGroupMemberIds(List<Long> memberList){
        String userList = StringUtils.join(memberList, ',');
        setUserList(userList);
    }
    // KEEP METHODS END
}
