package com.mymq.rabbitmq.workqueues;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * work queues 中扮演发送大量消息的生产者
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //模拟发送“大量”消息
    public static void main(String[] args) throws Exception {
        //获取channel
        Channel channel = RabbitMqUtils.getChannel();

        //队列的声明
        //通过信道生成队列
        //参数：1.队列名称  2.队列里面的消息是否持久化，默认消息存在内存中，即不持久化
        //     3.该队列是否只供一个消费者进行消费 是否进行消息共享  true表示可以多个消费者消费
        //     4.是否自动删除 最后一个消费者断开连接后，该队列是否自动删除  true 自动删除
        //     5.其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台输入消息
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String message = sc.next();
            //参数：1.发送到那个交换机 2.路由的key是那个 本次是队列名
            //     3.其他参数信息  4.发送消息的消息体
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完成：" + message);
        }

    }

}
