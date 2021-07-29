package com.example;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author CaoJing
 * @date 2021/07/26 14:54
 */
@SpringBootApplication
// 开启服务发现
@EnableDiscoveryClient
// 开启feign支持
@EnableFeignClients
// 允许事务
@EnableTransactionManagement
@EnableRabbit
public class OrderWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderWebApplication.class, args);
    }
}
