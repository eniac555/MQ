package com.mymq.rabbitmq.a05topic;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

/**
 * topic 模式
 * 消费者02
 * 声明主题交换机及消费者队列
 */
public class ReceiveLogsTopic02 {

    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare("Q2", false, false, false, null);
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息...");

        //接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            //System.out.println(new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：Q2" + "   绑定键：" + message.getEnvelope().getRoutingKey());
        };

        //取消消息
        CancelCallback cancelCallback = consumerTag -> {

        };

        channel.basicConsume("Q1", true, deliverCallback, cancelCallback);
    }
}
