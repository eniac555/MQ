package com.mymq.rabbitmq.a04routing;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

/**
 * routingKey = info、warning
 * 多重绑定，接收方之一
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个定向交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare("console", false, false, false, null);
        //绑定交换机和队列
        channel.queueBind("console", EXCHANGE_NAME, "info");//设置routingKey
        //多重绑定
        channel.queueBind("console", EXCHANGE_NAME, "warning");//设置routingKey
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上...");

        //接收消息触发的接口
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" +
                    new String(message.getBody(), "UTF-8"));
        };

        //取消接收消息接口
        CancelCallback cancelCallback = consumerTag -> {
        };

        channel.basicConsume("console", true, deliverCallback, cancelCallback);
    }
}
