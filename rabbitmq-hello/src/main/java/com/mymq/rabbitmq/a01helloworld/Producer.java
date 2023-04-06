package com.mymq.rabbitmq.a01helloworld;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发送消息
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂IP
        factory.setHost("192.168.163.128");
        //设置用户名
        factory.setUsername("admin");
        //设置密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //通过连接获取信道
        Channel channel = connection.createChannel();
        //通过信道生成队列
        //参数：1.队列名称  2.队列里面的消息是否持久化，默认消息存在内存中，即不持久化
        //     3.该队列是否只供一个消费者进行消费 是否进行消息共享  true表示可以多个消费者消费
        //     4.是否自动删除 最后一个消费者断开连接后，该队列是否自动删除  true 自动删除
        //     5.其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发消息
        String massage = "hello rabbitmq!";
        //参数：1.发送到那个交换机 2.路由的key是那个 本次是队列名
        //     3.其他参数信息  4.发送消息的消息体
        channel.basicPublish("", QUEUE_NAME, null, massage.getBytes());
        System.out.println("消息发送成功");

    }
}
