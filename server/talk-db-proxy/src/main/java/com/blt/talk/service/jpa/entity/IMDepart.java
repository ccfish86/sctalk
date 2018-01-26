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
 * The persistent class for the im_depart database table.
 * 
 */
@Entity
@Table(name = "im_depart")
@NamedQuery(name = "IMDepart.findAll", query = "SELECT i FROM IMDepart i")
public class IMDepart implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Column(name = "depart_name", nullable = false, length = 64)
    private String departName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private int updated;

    public IMDepart() {}

    public int getCreated() {
        return this.created;
    }

    public String getDepartName() {
        return this.departName;
    }

    public Long getId() {
        return this.id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public int getPriority() {
        return this.priority;
    }

    public byte getStatus() {
        return this.status;
    }

    public int getUpdated() {
        return this.updated;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

}
