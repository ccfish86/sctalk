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
 * The persistent class for the im_recent_session database table.
 * 
 */
@Entity
@Table(name = "im_recent_session")
@NamedQuery(name = "IMRecentSession.findAll", query = "SELECT i FROM IMRecentSession i")
public class IMRecentSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "peer_id", nullable = false)
    private Long peerId;

    private byte status;

    private byte type;

    @Column(nullable = false)
    private int updated;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public IMRecentSession() {}

    public int getCreated() {
        return this.created;
    }

    public Long getId() {
        return this.id;
    }

    public Long getPeerId() {
        return this.peerId;
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

    public void setCreated(int created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPeerId(Long peerId) {
        this.peerId = peerId;
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
