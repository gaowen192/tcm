package com.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * 千行服务启动类
 * Spring Boot应用的主入口点
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages= {"com.ride"})
public class QianxingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QianxingServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("===============================================");
        System.out.println("千行服务启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("Swagger文档地址：http://localhost:8080/api/swagger-ui.html");
        System.out.println("===============================================");
    }
}
