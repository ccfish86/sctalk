/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
public abstract class IMAbstractMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 4096)
    private String content;

    @Column(nullable = false)
    private int created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "msg_id", nullable = false)
    private Long msgId;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private byte type;

    @Column(nullable = false)
    private int updated;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public String getContent() {
        return this.content;
    }

    public int getCreated() {
        return this.created;
    }

    public Long getId() {
        return this.id;
    }

    public Long getMsgId() {
        return this.msgId;
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

    public Long getUserId() {
        return this.userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
