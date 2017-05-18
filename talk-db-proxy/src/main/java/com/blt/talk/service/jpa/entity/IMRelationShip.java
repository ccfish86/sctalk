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
 * The persistent class for the im_relation_ship database table.
 * 
 */
@Entity
@Table(name = "im_relation_ship")
@NamedQuery(name = "IMRelationShip.findAll", query = "SELECT i FROM IMRelationShip i")
public class IMRelationShip implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "big_id", nullable = false)
    private Long bigId;

    @Column(nullable = false)
    private int created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "small_id", nullable = false)
    private Long smallId;

    private byte status;

    @Column(nullable = false)
    private int updated;

    public IMRelationShip() {}

    public Long getBigId() {
        return this.bigId;
    }

    public int getCreated() {
        return this.created;
    }

    public Long getId() {
        return this.id;
    }

    public Long getSmallId() {
        return this.smallId;
    }

    public byte getStatus() {
        return this.status;
    }

    public int getUpdated() {
        return this.updated;
    }

    public void setBigId(Long bigId) {
        this.bigId = bigId;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSmallId(Long smallId) {
        this.smallId = smallId;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

}
