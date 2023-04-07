package com.mymq.rabbitmq.a03pubandsub;

import com.mymq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 交换机 发消息
 */
public class EmitLog {
    //交换机名
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null,
                    message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
