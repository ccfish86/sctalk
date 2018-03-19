package com.grpc.java.kernel.entity;

public class role_power_info {
    private Integer id;

    private Integer roleId;

    private Integer powerId;

    public role_power_info(){

    }

    public role_power_info(Integer id, Integer roleId, Integer powerId) {
        this.id = id;
        this.roleId = roleId;
        this.powerId = powerId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Integer getPowerId() {
        return powerId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }
}