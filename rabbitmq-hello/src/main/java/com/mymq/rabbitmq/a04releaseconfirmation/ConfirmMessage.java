package com.mymq.rabbitmq.a04releaseconfirmation;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;

/**
 * 验证发布确认的不同模式
 * 1.单个确认     发布1000个单独确认消息，耗时：565ms
 * 2.批量确认     发布1000个批量确认消息，耗时：66ms
 * 3.异步批量确认  发布1000个异步确认消息，耗时：33ms
 * 利用耗时进行比较
 */
public class ConfirmMessage {
    //批量发送消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        //confirmForIndividual();
        //2.批量确认
        //confirmForBatch();
        //3.异步批量确认
        confirmForBatchAsync();

    }

    //1.单个确认
    public static void confirmForIndividual() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时：" + (end - start) + "ms");

    }


    //2.批量确认
    public static void confirmForBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();
        //批量确认消息的大小
        int batchSize = 100;
        //批量发送，批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            //判断达到100条消息时，批量确认
            if (i % batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
                //100条确认一次
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (end - start) + "ms");
    }


    //3.批量异步确认
    public static void confirmForBatchAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认模式
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        //消息确认成功的回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //参数1：消息的标记  参数2：是否批量确认
            System.out.println("已经成功确认的消息" + deliveryTag);
        };
        //消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息" + deliveryTag);
        };

        //消息监听器，监听哪些消息成功，哪些消息失败
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时：" + (end - start) + "ms");

    }


}
