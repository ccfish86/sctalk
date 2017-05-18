package com.blt.talk.router.server;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class RouterServerApplication {

	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(RouterServerApplication.class); 
        ApplicationContext applicationContext = app.run(args);
	    
	    try {
            applicationContext.getBean(RouterServer.class).start();
            System.out.println("done...");
        } catch (BeansException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
