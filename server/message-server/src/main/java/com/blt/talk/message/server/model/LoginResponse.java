/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.model;

/**
 * 登录结果
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public final class LoginResponse {

    /** 消息服务器IP */
    private String priorIP;
    /** 消息服务器备用IP */
    private String backupIP;
    /** 文件服务器IP上传地址 */
    private String msfsPrior;
    /** 文件服务器备用上传地址 */
    private String msfsBackup;
    /** 消息服务器端口号 */
    private Integer port;

    private String discovery;

    /**
     * @return the discovery
     */
    public String getDiscovery() {
        return discovery;
    }

    /**
     * @param discovery the discovery to set
     */
    public void setDiscovery(String discovery) {
        this.discovery = discovery;
    }

    /**
     * @return the priorIP
     */
    public String getPriorIP() {
        return priorIP;
    }

    /**
     * @param priorIP the priorIP to set
     */
    public void setPriorIP(String priorIP) {
        this.priorIP = priorIP;
    }

    /**
     * @return the backupIP
     */
    public String getBackupIP() {
        return backupIP;
    }

    /**
     * @param backupIP the backupIP to set
     */
    public void setBackupIP(String backupIP) {
        this.backupIP = backupIP;
    }

    /**
     * @return the msfsPrior
     */
    public String getMsfsPrior() {
        return msfsPrior;
    }

    /**
     * @param msfsPrior the msfsPrior to set
     */
    public void setMsfsPrior(String msfsPrior) {
        this.msfsPrior = msfsPrior;
    }

    /**
     * @return the msfsBackup
     */
    public String getMsfsBackup() {
        return msfsBackup;
    }

    /**
     * @param msfsBackup the msfsBackup to set
     */
    public void setMsfsBackup(String msfsBackup) {
        this.msfsBackup = msfsBackup;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

}
