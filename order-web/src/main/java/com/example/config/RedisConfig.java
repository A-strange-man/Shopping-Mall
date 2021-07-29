package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author CaoJing
 * @date 2021/07/26 15:11
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    // 缓存操作组件RedisTemplate的自定义配置
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 指定key序列化策略为String序列化，value为JDK自带的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        // 指定hashkey的序列化策略为string序列化---针对hash散列存储
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    // 缓存操作组件stringRedisTemplate
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        // 采用默认配置
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
