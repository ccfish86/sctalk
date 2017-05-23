package com.blt.talk.message.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Configuration
@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
public class MessageServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(MessageServerApplication.class);
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MessageServerApplication.class); 
        app.setWebEnvironment(false);
        ApplicationContext applicationContext = app.run(args);
        
        try {
            applicationContext.getBean(MessageServerStarter.class).start(args);
            applicationContext.getBean(RouterServerConnecter.class).start(args);
        } catch (Exception e) {
            logger.error("Shutdown with errors ", e);
            SpringApplication.exit(applicationContext);
        } finally {
            logger.info("done");
        }
    }
    
}
