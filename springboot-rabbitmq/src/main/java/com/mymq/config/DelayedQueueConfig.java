package com.mymq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟消息插件设置延迟队列的配置类
 */
@Configuration
public class DelayedQueueConfig {
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //延迟交换机
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";


    //声明交换机，基于消息延迟插件
    @Bean
    public CustomExchange delayedExchange() {//类型为自定义类型
        Map<String, Object> arguments = new HashMap<>(3);
        //定义延迟类型
        arguments.put("x-delayed-type", "direct");

        //参数：1.交换机名称，2.交换机类型，3.是否持久化，4.是否自动删除，5.map集合参数
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message",
                true, false, arguments);
    }


    //声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }


    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue queue,
            @Qualifier("delayedExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
