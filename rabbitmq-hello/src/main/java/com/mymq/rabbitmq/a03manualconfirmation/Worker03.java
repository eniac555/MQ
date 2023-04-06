package com.mymq.rabbitmq.a03manualconfirmation;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.mymq.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消息在手动应答时是不丢失的，放回队列重新消费
 */
public class Worker03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        //获得信道
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1正在等待接收消息...时间较短");

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡1秒
            SleepUtils.sleep(1);
            System.out.println("接收的消息为：" + new String(message.getBody()));//打印接收的消息
            //手动应答
            //参数1：消息的标记tag  参数2：是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费取消/中断");
        };

        //设置不公平分发
        //channel.basicQos(1);//能者多劳模式，默认是0，公平分发，1表示不公平分发

        //设置预取值
        channel.basicQos(2);//和前面的不公平分发不能同时存在


        //接收消息  手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
