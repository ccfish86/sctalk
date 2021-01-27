/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */
package com.blt.talk.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * DB-proxy启动类
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.blt.talk.common.io", "com.blt.talk.service"})
@EnableAutoConfiguration
@SpringBootApplication
public class TalkDbProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkDbProxyApplication.class, args);
    }

    /**
     * 用户密码加密/解密类
     * 
     * @return 用户密码处理相关类
     * @since 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}
