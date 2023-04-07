package com.mymq.rabbitmq.a06deadqueue.a01ttl;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列的生产者
 */
public class ProduceDead {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //死信消息  设置ttl时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();//ms

        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan",
                    properties, message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
