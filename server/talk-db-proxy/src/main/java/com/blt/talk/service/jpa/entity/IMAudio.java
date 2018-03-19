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
 * The persistent class for the im_audio database table.
 * 
 */
@Entity
@Table(name = "im_audio")
@NamedQuery(name = "IMAudio.findAll", query = "SELECT i FROM IMAudio i")
public class IMAudio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Column(nullable = false)
    private int duration;

    @Column(name = "from_id", nullable = false)
    private Long fromId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(length = 255)
    private String path;

    @Column(nullable = false)
    private int size;

    @Column(name = "to_id", nullable = false)
    private Long toId;

    public IMAudio() {}

    public int getCreated() {
        return this.created;
    }

    public int getDuration() {
        return this.duration;
    }

    public Long getFromId() {
        return this.fromId;
    }

    public Long getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public int getSize() {
        return this.size;
    }

    public Long getToId() {
        return this.toId;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

}
