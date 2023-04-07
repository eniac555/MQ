package com.mymq.rabbitmq.a06deadqueue.a02outlength;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 * 接收正常消息的消费者
 */
public class Consumer01 {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange_out_length";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange_out_length";
    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue_out_length";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue_out_length";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //过期时间
        //arguments.put("x-message-ttl",10000);
        //正常队列应该设置过期之后用到的死信交换机是谁
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");

        //设置正常队列的最大长度
        arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false,
                false, arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false,
                null);

        //声明不同的绑定关系
        //绑定普通交换机和队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //绑定死信的交换机和队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("消费者01接收的消息为："
                    + new String(message.getBody(), StandardCharsets.UTF_8));
        };

        CancelCallback cancelCallback = consumerTag -> {
        };

        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
    }
}
