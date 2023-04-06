package com.mymq.rabbitmq.a02workqueues;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 这是一个工作线程，相当一一个消费者
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        //获取channel
        Channel channel = RabbitMqUtils.getChannel();

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收的消息为：" + new String(message.getBody()));//打印接收的消息
        };

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费取消/中断");
        };

        //消息接收
        //参数：
        //1.消费那个队列  2.消费成功之后是否自动应答，true代表自动应答
        //3.消费者未消费成功的回调  4.消费者取消消费的回调
        System.out.println("Worker02正在等待消息...");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
