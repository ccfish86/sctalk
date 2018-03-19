/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package net.ccfish.talk.admin.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the manager_info database table.
 * 
 */
@Entity
@Table(name = "manager_info")
@NamedQuery(name = "ManagerUser.findAll", query = "SELECT m FROM ManagerUser m")
public class ManagerUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id", unique = true, nullable = false)
    private int managerId;

    @Column(nullable = false, length = 100)
    private String avatar;

    @Column(nullable = false, length = 40)
    private String introduction;

    @JsonIgnore
    @Column(nullable = false, length = 32, columnDefinition = "CHAR")
    private String password;

    @Column(nullable = false, length = 40)
    private String token;

    @Column(nullable = false, length = 40)
    private String username;

    @JsonIgnore
    @ManyToMany(targetEntity = RoleInfo.class, fetch = FetchType.EAGER)
    @JoinTable(name = "manager_role_info",
            joinColumns = {@JoinColumn(name = "manager_id", referencedColumnName = "manager_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    @BatchSize(size = 20)
    private Set<RoleInfo> roles = new HashSet<>();

    public ManagerUser() {}

    public int getManagerId() {
        return this.managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the roles
     */
    public Set<RoleInfo> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<RoleInfo> roles) {
        this.roles = roles;
    }
}
