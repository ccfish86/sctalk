/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.grpc.java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * talk-grpc application starter
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.grpc.java.kernel.mybatis.mapper"})
@SpringBootApplication
public class TalkGrpcApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TalkGrpcApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }
}
