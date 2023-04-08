package com.mymq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL队列的配置类代码
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机名称
    public static final String X_EXCHANGE = "X";

    //死信交换机名称
    public static final String Y_DEAD_EXCHANGE = "Y";

    //普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";

    //死信队列名称
    public static final String DEAD_QUEUE = "QD";

    //新增一个普通队列，无过期时间设置，需要发送者自己设定
    public static final String QUEUE_C = "QC";

    //声明普通交换机   xExchange别名
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }


    //声明死信交换机   yExchange别名
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }

    //声明普通队列A
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        //过期时间
        arguments.put("x-message-ttl", 10000);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }


    //声明普通队列B
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        //过期时间
        arguments.put("x-message-ttl", 40000);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //声明死信队列
    @Bean("deadQueue")
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }


    //声明普通队列C
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //绑定关系
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定关系
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定关系
    @Bean
    public Binding deadQueueBindingY(@Qualifier("deadQueue") Queue deadQueue,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(deadQueue).to(yExchange).with("YD");
    }


    //绑定关系
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


}
