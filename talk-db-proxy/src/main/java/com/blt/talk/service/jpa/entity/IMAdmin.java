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
 * The persistent class for the im_admin database table.
 * 
 */
@Entity
@Table(name = "im_admin")
@NamedQuery(name = "IMAdmin.findAll", query = "SELECT i FROM IMAdmin i")
public class IMAdmin implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 256)
    private String pwd;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false, length = 40)
    private String uname;

    @Column(nullable = false)
    private int updated;

    public IMAdmin() {}

    public int getCreated() {
        return this.created;
    }

    public int getId() {
        return this.id;
    }

    public String getPwd() {
        return this.pwd;
    }

    public byte getStatus() {
        return this.status;
    }

    public String getUname() {
        return this.uname;
    }

    public int getUpdated() {
        return this.updated;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

}
