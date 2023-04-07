package com.mymq.rabbitmq.deadqueue.a02outlength;


import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列的第二个消费者，用来消费死信队列里面的消息
 */
public class Consumer02 {

    //死信队列
    public static final String DEAD_QUEUE = "dead_queue_out_length";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("消费者02接收的消息为："
                    + new String(message.getBody(), StandardCharsets.UTF_8));
        };

        CancelCallback cancelCallback = consumerTag -> {
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
