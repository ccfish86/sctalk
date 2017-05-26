package com.blt.talk.service.jpa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the im_group database table.
 * 
 */
@Entity
@Table(name = "im_group")
@NamedQuery(name = "IMGroup.findAll", query = "SELECT i FROM IMGroup i")
public class IMGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 256)
    private String avatar;

    @Column(nullable = false)
    private int created;

    @Column(nullable = false)
    private Long creator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "last_chated", nullable = false)
    private int lastChated;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private byte type;

    @Column(nullable = false)
    private int updated;

    @Column(name = "user_cnt", nullable = false)
    private int userCnt;

    @Column(nullable = false)
    private int version;

    @OneToMany(targetEntity=IMGroupMember.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="group_id", referencedColumnName="id")
    private List<IMGroupMember> groupMemberList; 
    
    public IMGroup() {}

    public String getAvatar() {
        return this.avatar;
    }

    public int getCreated() {
        return this.created;
    }

    public Long getCreator() {
        return this.creator;
    }

    public Long getId() {
        return this.id;
    }

    public int getLastChated() {
        return this.lastChated;
    }

    public String getName() {
        return this.name;
    }

    public byte getStatus() {
        return this.status;
    }

    public byte getType() {
        return this.type;
    }

    public int getUpdated() {
        return this.updated;
    }

    public int getUserCnt() {
        return this.userCnt;
    }

    public int getVersion() {
        return this.version;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastChated(int lastChated) {
        this.lastChated = lastChated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the groupMemberList
     */
    public List<IMGroupMember> getGroupMemberList() {
        return groupMemberList;
    }

    /**
     * @param groupMemberList the groupMemberList to set
     */
    public void setGroupMemberList(List<IMGroupMember> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

}
