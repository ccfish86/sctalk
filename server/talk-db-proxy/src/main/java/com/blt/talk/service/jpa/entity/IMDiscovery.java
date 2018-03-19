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
 * The persistent class for the im_discovery database table.
 * 
 */
@Entity
@Table(name = "im_discovery")
@NamedQuery(name = "IMDiscovery.findAll", query = "SELECT i FROM IMDiscovery i")
public class IMDiscovery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "item_name", nullable = false, length = 64)
    private String itemName;

    @Column(name = "item_priority", nullable = false)
    private int itemPriority;

    @Column(name = "item_url", nullable = false, length = 64)
    private String itemUrl;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private int updated;

    public IMDiscovery() {}

    public int getCreated() {
        return this.created;
    }

    public Long getId() {
        return this.id;
    }

    public String getItemName() {
        return this.itemName;
    }

    public int getItemPriority() {
        return this.itemPriority;
    }

    public String getItemUrl() {
        return this.itemUrl;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPriority(int itemPriority) {
        this.itemPriority = itemPriority;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

}
