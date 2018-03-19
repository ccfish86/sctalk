package com.webjava.kernel.entity;

public class IMGroup {
    private Integer id;

    private String name="";

    private String avatar="";

    private Integer creator=0 ;

    private Byte type=0;

    private Integer usercnt;

    private Byte status=0;

    private Integer version=0;

    private Integer lastchated=0;

    private Integer updated=0;

    private Integer created=0;

    public IMGroup(){

    }

    public IMGroup(Integer id, String name, String avatar, Integer creator, Byte type, Integer usercnt, Byte status, Integer version, Integer lastchated, Integer updated, Integer created) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.creator = creator;
        this.type = type;
        this.usercnt = usercnt;
        this.status = status;
        this.version = version;
        this.lastchated = lastchated;
        this.updated = updated;
        this.created = created;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setUsercnt(Integer usercnt) {
        this.usercnt = usercnt;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setLastchated(Integer lastchated) {
        this.lastchated = lastchated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getCreator() {
        return creator;
    }

    public Byte getType() {
        return type;
    }

    public Integer getUsercnt() {
        return usercnt;
    }

    public Byte getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public Integer getLastchated() {
        return lastchated;
    }

    public Integer getUpdated() {
        return updated;
    }

    public Integer getCreated() {
        return created;
    }
}