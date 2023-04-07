package com.mymq.rabbitmq.a03pubandsub;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 接收消息2
 * 不设置routingKey，两个消费者都能收到
 */
public class ReceiveLog2 {
    //交换机名
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明队列  临时队列
        //生成一个临时队列，队列名称随机，当消费者断开与队列的连接，队列就自动删除
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");//不设置routingKey，两个消费者都能收到
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上");

        //接收消息触发的接口
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("02控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };

        //取消接收消息接口
        CancelCallback cancelCallback = consumerTag->{};

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

    }
}
