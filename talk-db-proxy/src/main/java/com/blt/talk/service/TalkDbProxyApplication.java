package com.blt.talk.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@EnableDiscoveryClient
@EnableAutoConfiguration
@SpringBootApplication
public class TalkDbProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkDbProxyApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new StandardPasswordEncoder();
	}
}
