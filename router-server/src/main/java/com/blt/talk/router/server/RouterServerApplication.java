package com.blt.talk.router.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RouterServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RouterServerApplication.class);
        app.run(args);
    }
}
