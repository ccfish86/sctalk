package com.grpc.java.kernel.entity;

public class IMUser {

    private Integer id;

    private Byte sex=0;

    private String name;

    private String domain="0";

    private String nick="";

    private String password=null;

    private String salt="";

    private String phone="";

    private String email="";

    private String avatar="";

    private Integer departid=0;

    private Byte status=0;

    private Integer created=0;

    private Integer updated=0;

    private Boolean pushShieldStatus=true;

    private String signInfo="";


    public IMUser(){

    }

    public IMUser(Integer id, Byte sex, String name, String domain, String nick, String password, String salt, String phone, String email, String avatar, Integer departid, Byte status, Integer created, Integer updated, Boolean pushShieldStatus, String signInfo) {
        this.id = id;
        this.sex = sex;
        this.name = name;
        this.domain = domain;
        this.nick = nick;
        this.password = password;
        this.salt = salt;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.departid = departid;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.pushShieldStatus = pushShieldStatus;
        this.signInfo = signInfo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setdepartid(Integer departid) {
        this.departid = departid;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public void setPushShieldStatus(Boolean pushShieldStatus) {
        this.pushShieldStatus = pushShieldStatus;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public Integer getId() {
        return id;
    }

    public Byte getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getdepartid() {
        return departid;
    }

    public Byte getStatus() {
        return status;
    }

    public Integer getCreated() {
        return created;
    }

    public Integer getUpdated() {
        return updated;
    }

    public Boolean getPushShieldStatus() {
        return pushShieldStatus;
    }

    public String getSignInfo() {
        return signInfo;
    }
}