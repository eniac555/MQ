package com.mymq.rabbitmq.deadqueue.a02outlength;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列的生产者
 */
public class ProduceDeadOutLength {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange_out_length";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan",
                    null, message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
