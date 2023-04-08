package com.mymq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认高级内容--配置类
 */
@Configuration
public class ConfirmConfig {
    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    //路由key
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //交换机声明
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    //队列声明
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定关系
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue queue,
                                        @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }


}
