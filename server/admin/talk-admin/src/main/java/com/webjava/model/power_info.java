package com.webjava.model;

public class power_info {
    private Integer powerId;

    private String powerName;

    private String powerUrl;

    private Integer parentId;

    public power_info(){

    }

    public power_info(Integer powerId, String powerName, String powerUrl, Integer parentId) {
        this.powerId = powerId;
        this.powerName = powerName;
        this.powerUrl = powerUrl;
        this.parentId = parentId;
    }

    public Integer getPowerId() {
        return powerId;
    }

    public String getPowerName() {
        return powerName;
    }

    public String getPowerUrl() {
        return powerUrl;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public void setPowerUrl(String powerUrl) {
        this.powerUrl = powerUrl;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}