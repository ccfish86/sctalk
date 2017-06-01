/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 服务启动类
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class RouterServerApplication {

    /**
     * 程序入口
     * @param args 命令行参数
     * @since  1.0
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RouterServerApplication.class);
        app.run(args);
    }
}
