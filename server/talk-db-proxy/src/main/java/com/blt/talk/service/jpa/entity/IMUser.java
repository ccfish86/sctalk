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
 * The persistent class for the im_user database table.
 * 
 */
@Entity
@Table(name = "im_user")
@NamedQuery(name = "IMUser.findAll", query = "SELECT i FROM IMUser i")
public class IMUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 255)
    private String avatar;

    @Column(nullable = false)
    private int created;

    @Column(name = "depart_id", nullable = false)
    private Long departId;

    @Column(nullable = false, length = 32)
    private String domain;

    @Column(nullable = false, length = 64)
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false, length = 32)
    private String nick;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(name = "push_shield_status", nullable = false)
    private byte pushShieldStatus;

    @Column(nullable = false, length = 4)
    private String salt;

    @Column(nullable = false)
    private byte sex;

    @Column(name = "sign_info", nullable = false, length = 128)
    private String signInfo;

    private byte status;

    @Column(nullable = false)
    private int updated;

    public IMUser() {}

    public String getAvatar() {
        return this.avatar;
    }

    public int getCreated() {
        return this.created;
    }

    public Long getDepartId() {
        return this.departId;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getEmail() {
        return this.email;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNick() {
        return this.nick;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhone() {
        return this.phone;
    }

    public byte getPushShieldStatus() {
        return this.pushShieldStatus;
    }

    public String getSalt() {
        return this.salt;
    }

    public byte getSex() {
        return this.sex;
    }

    public String getSignInfo() {
        return this.signInfo;
    }

    public byte getStatus() {
        return this.status;
    }

    public int getUpdated() {
        return this.updated;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setDepartId(Long departId) {
        this.departId = departId;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPushShieldStatus(byte pushShieldStatus) {
        this.pushShieldStatus = pushShieldStatus;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

}
