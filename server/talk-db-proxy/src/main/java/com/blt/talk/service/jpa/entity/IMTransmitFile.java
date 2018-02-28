/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

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
 * 
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
@Entity
@Table(name = "im_transmit_file")
@NamedQuery(name = "IMTransmitFile.findAll", query = "SELECT i FROM IMTransmitFile i")
public class IMTransmitFile implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    /** 更新时间 */
    @Column(nullable = false)
    private long updated;

    /** 用户ID */
    @Column(name = "from_id", nullable = false)
    private Long fromId;
    
    /** 目标ID */
    @Column(name = "to_id", nullable = false)
    private Long toId;
    
    /** 大小 */
    @Column(nullable = false)
    private int size;

    /**任务ID*/
    @Column(name = "task_id", nullable = false)
    private String taskId;

    /** 状态 */
    @Column(nullable = false)
    private int status;
    
    /** 创建时间 */
    @Column(nullable = false)
    private long created;

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
     * @return the updated
     */
    public long getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(long updated) {
        this.updated = updated;
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
     * @return the toId
     */
    public Long getToId() {
        return toId;
    }

    /**
     * @param toId the toId to set
     */
    public void setToId(Long toId) {
        this.toId = toId;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the created
     */
    public long getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(long created) {
        this.created = created;
    }
    
}
