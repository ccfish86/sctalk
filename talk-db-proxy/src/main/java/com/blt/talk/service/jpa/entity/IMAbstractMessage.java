/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 消息相关表的通用实体类
 * <br>
 * 处理消息分表存储通用字段
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
public abstract class IMAbstractMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    
    /** 内容 */
    @Column(nullable = false, length = 4096)
    private String content;

    /** 创建时间 */
    @Column(nullable = false)
    private int created;

    /** 消息ID（每两个用户间或每个组自增） */
    @Column(name = "msg_id", nullable = false)
    private Long msgId;

    /** 消息状态 */
    @Column(nullable = false)
    private byte status;

    /** 消息类型（文本/语音）*/
    @Column(nullable = false)
    private byte type;

    /** 更新时间 */
    @Column(nullable = false)
    private int updated;

    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

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
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the 创建时间
     */
    public int getCreated() {
        return created;
    }

    /**
     * @param created 创建时间
     */
    public void setCreated(int created) {
        this.created = created;
    }

    /**
     * @return the 消息ID
     */
    public Long getMsgId() {
        return msgId;
    }

    /**
     * @param msgId 消息ID
     */
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the 状态
     */
    public byte getStatus() {
        return status;
    }

    /**
     * @param status 状态
     */
    public void setStatus(byte status) {
        this.status = status;
    }

    /**
     * @return the 类型
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type 类型
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the 更新时间
     */
    public int getUpdated() {
        return updated;
    }

    /**
     * @param updated 更新时间
     */
    public void setUpdated(int updated) {
        this.updated = updated;
    }

    /**
     * @return the 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
