package com.grpc.java.kernel.entity;

public class IMDepart {
    private Integer id;

    private String departname="";

    private Integer priority;

    private Integer parentid=0;

    private Integer status=0;

    private Integer created=0;

    private Integer updated=0;

    public IMDepart(){

    }

    public IMDepart(Integer id, String departname, Integer priority, Integer parentid, Integer status, Integer created, Integer updated) {
        this.id = id;
        this.departname = departname;
        this.priority = priority;
        this.parentid = parentid;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }



    public Integer getId() {
        return id;
    }

    public String getDepartname() {
        return departname;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getParentid() {
        return parentid;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCreated() {
        return created;
    }

    public Integer getUpdated() {
        return updated;
    }
}