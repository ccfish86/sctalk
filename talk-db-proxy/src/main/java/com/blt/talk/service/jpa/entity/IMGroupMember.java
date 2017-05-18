package com.blt.talk.service.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_member database table.
 * 
 */
@Entity
@Table(name = "im_group_member")
@NamedQuery(name = "IMGroupMember.findAll", query = "SELECT i FROM IMGroupMember i")
public class IMGroupMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private int updated;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public IMGroupMember() {}

    public int getCreated() {
        return this.created;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public Long getId() {
        return this.id;
    }

    public byte getStatus() {
        return this.status;
    }

    public int getUpdated() {
        return this.updated;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
