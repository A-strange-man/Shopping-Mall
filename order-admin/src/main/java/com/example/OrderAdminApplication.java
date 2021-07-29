package com.example;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author CaoJing
 * @date 2021/07/26 14:55
 */
@SpringBootApplication
// 开启服务发现
@EnableDiscoveryClient
// 开启feign支持
@EnableFeignClients
@EnableRabbit
public class OrderAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderAdminApplication.class, args);
    }
}
