package com.grpc.java.kernel.entity;

public class manager_info {
    private Integer managerId;

    private String username;

    private String password;

    private String token;

    private String introduction;

    private String avatar;

    public manager_info(){

    }

    public manager_info(Integer managerId, String username, String password, String token, String introduction, String avatar) {
        this.managerId = managerId;
        this.username = username;
        this.password = password;
        this.token = token;
        this.introduction = introduction;
        this.avatar = avatar;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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
}