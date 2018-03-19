package com.grpc.java.kernel.entity;

public class IMDiscovery {
    private Integer id;

    private String itemname="";

    private String itemurl="";

    private Integer itempriority=0;

    private Integer status=0;

    private Integer created=0;

    private Integer updated=0;

    public IMDiscovery(){

    }

    public IMDiscovery(Integer id, String itemname, String itemurl, Integer itempriority, Integer status, Integer created, Integer updated) {
        this.id = id;
        this.itemname = itemname;
        this.itemurl = itemurl;
        this.itempriority = itempriority;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public void setItemurl(String itemurl) {
        this.itemurl = itemurl;
    }

    public void setItempriority(Integer itempriority) {
        this.itempriority = itempriority;
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

    public String getItemname() {
        return itemname;
    }

    public String getItemurl() {
        return itemurl;
    }

    public Integer getItempriority() {
        return itempriority;
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