package com.mymq.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 获得Channel的工具类
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws Exception {
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
        return connection.createChannel();
    }
}
