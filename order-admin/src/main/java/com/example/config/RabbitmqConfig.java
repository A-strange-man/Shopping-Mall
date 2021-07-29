package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author CaoJing
 * @date 2021/07/29 8:53
 */
@Configuration
public class RabbitmqConfig {
    // 定义日志
    private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);

    // 自动装配RabbitMQ的链接工厂实例
    @Autowired
    private CachingConnectionFactory connectionFactory;

    // 自动装配消息监听器所在的容器工厂配置类实例
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    // 定义读取配置文件的环境变量实例
    @Autowired
    private Environment env;

    /**
     * 创建队列
     * @return
     */
    @Bean(name = "queue")
    public Queue queue() {
        return new Queue(Objects.requireNonNull(env.getProperty("mq.direct.queue.one.name")), true);
    }

    /**
     * 创建交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(env.getProperty("mq.direct.exchange.name"), true, false);
    }

    /**
     * 创建绑定
     * @return
     */
    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(env.getProperty("mq.direct.routing.key.one.name"));
    }

    /**
     * 单一消费者实例的配置
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        // 定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        // 设置消息在传输中的格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置并发消费者实例的初始数量为1
        factory.setConcurrentConsumers(1);
        // 设置并发消费者实例的最大数量为1
        factory.setMaxConcurrentConsumers(1);
        // 设置并发消费者实例中每个实例拉取的消息数量为 1个
        factory.setPrefetchCount(1);
        return factory;
    }

    /**
     * 多个消费者实例的配置 -针对高并发业务场景
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        // 定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 设置容器工厂所用的实例
        factoryConfigurer.configure(factory, connectionFactory);
        // 设置消息在传输中的格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置消息的确认消费模式。 这里为NONE，表示不需要确认消费
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        // 设置并发消费者实例的初始数量为10
        factory.setConcurrentConsumers(10);
        // 设置并发消费者实例的最大数量为15
        factory.setMaxConcurrentConsumers(15);
        // 设置并发消费者实例中每个实例拉取的消息数量为 10个
        factory.setPrefetchCount(10);
        return factory;
    }

    /**
     * 自定义RabbitMQ发送消息的操作组件 rabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        // 设置 “发送消息后进行确认”
        connectionFactory.setPublisherConfirms(true);
        // 设置 “发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);
        // 构造发送消息组件实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        // 发送消息成功 打印日志
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                log.info("消息发送成功：CorrelationData({}), ack({}), cause({})", correlationData, b, s);
            }
        });
        // 消息发送失败，打印日志
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.info("消息丢失：exchange({}), route({}), replayCode({}), replayText({}), message({})", s1, s2, i, s, message);
            }
        });
        return rabbitTemplate;
    }
}
