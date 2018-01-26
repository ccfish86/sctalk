package com.webjava.model;

public class manager_role_info {
    private Integer id;

    private Integer managerId;

    private Integer roleId;

    public manager_role_info(){

    }

    public manager_role_info(Integer id, Integer managerId, Integer roleId) {
        this.id = id;
        this.managerId = managerId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}