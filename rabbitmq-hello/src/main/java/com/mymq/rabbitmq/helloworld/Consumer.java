package com.mymq.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者：接收消息
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

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

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(new String(message.getBody()));//打印接收的消息
        };

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费取消/中断");
        };

        //消费者消费消息
        //参数：1.消费那个队列  2.消费成功之后是否自动应答，true代表自动应答
        //3.消费者未消费成功的回调  4.消费者取消消费的回调
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
