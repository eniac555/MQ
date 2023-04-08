package com.mymq.rabbitmq.priorityqueue;


import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者：发送消息
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //设置优先级
        Map<String, Object> map = new HashMap<>();
        map.put("x-max-priority", 10);//原则上是0-255，这里设置0-10

        //通过信道生成队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, map);

        //发消息
        for (int i = 1; i < 11; i++) {
            String massage = "hello rabbitmq - " + i;
            if (i == 5) {
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, massage.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, massage.getBytes());
            }
        }
        System.out.println("消息发送成功");

    }
}
