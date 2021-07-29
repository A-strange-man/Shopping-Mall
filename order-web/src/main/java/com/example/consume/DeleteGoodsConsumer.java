package com.example.consume;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/29 9:26
 */
@Component
public class DeleteGoodsConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeleteGoodsConsumer.class);

    // 商品统一前缀
    private static final String keyPrefix = "goods:tec_14536:";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从缓存删除商品信息
     * @param id
     */
    @RabbitListener(queues = "${mq.direct.queue.one.name}", containerFactory = "singleListenerContainer")
    public void deleteGoods(@Payload BigInteger id) {

        String key = keyPrefix + id;
        boolean existKey = redisTemplate.hasKey(key);
        log.info("进入到删除缓存");

        if (existKey) {
            redisTemplate.delete(key);
            log.info("缓存中对应的商品删除-id={}", id);
        }
    }
}
