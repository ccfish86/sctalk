/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 路由服务配置
 * 
 * @author 袁贵
 * @version 3.0
 * @since 3.0
 */
@Component
@ConfigurationProperties(prefix = "talk.router")
public class RouterServerConfig {

    /** IP地址 */
    private String ip;
    /** 端口号 */
    private int port;

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }


}
