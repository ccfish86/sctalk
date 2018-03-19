package com.webjava.kernel.entity;

import java.util.Date;

public class IMManager {
    private Integer id;

    private String username;

    private String password;

    private Integer roleId;

    private String role;

    private String token;

    private String introduction;

    private String avatar;

    private Byte status;

    private Date created;

    private Date updated;

    public IMManager(){

    }

    public IMManager(Integer id, String username, String password, Integer roleId, String role, String token, String introduction, String avatar, Byte status, Date created, Date updated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.role = role;
        this.token = token;
        this.introduction = introduction;
        this.avatar = avatar;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public Byte getStatus() {
        return status;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}