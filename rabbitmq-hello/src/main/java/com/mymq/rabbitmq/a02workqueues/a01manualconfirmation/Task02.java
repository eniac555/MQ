package com.mymq.rabbitmq.a02workqueues.a01manualconfirmation;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 消息在手动应答时是不丢失的，放回队列重新消费
 */
public class Task02 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //发送消息
    public static void main(String[] args) throws Exception {
        //获得信道
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明

        boolean durable = true;//队列持久化，rabbit服务宕机，队列依然存在

        channel.queueDeclare(TASK_QUEUE_NAME, false, false,
                false, null);

        //开启发布确认
        channel.confirmSelect();//前提是生产者和消费者都进行了持久化设置

        //从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null,
                    message.getBytes("UTF-8"));

            //channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    //message.getBytes("UTF-8"));//告诉消费者这个消息需要持久化

            System.out.println("生产者发出消息：" + message);
        }
    }
}
