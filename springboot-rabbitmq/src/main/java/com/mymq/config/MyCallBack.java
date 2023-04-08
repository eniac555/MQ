package com.mymq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 回调接口:ConfirmCallback
 * <p>
 * 回退接口:ReturnCallback
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    //这里虽然实现了接口，但是想让rabbitTemplate要调用，需要将容器里的引用指向这里实现的这个类，不然会指向未实现的类
    //需要把自己实现的这个类，注入到rabbitTemplate中去，不然rabbitTemplate中的还是未实现的类

    @Resource
    private RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void init() {
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }


    //交换机确认回调的方法，成功或者失败都要回调
    //1.发消息  交换机收到了  回调
    // 1.1 correlationData  保存回调消息的ID及相关信息
    // 1.2 ack 表示交换机是否收到消息  true为收到了
    // 1.3 cause 失败的原因，成功时为null

    //2.发消息  交换机没有收到  也进行回调
    // 2.1 correlationData  保存回调消息的ID及相关信息
    // 2.2 ack 表示交换机是否收到消息  true为收到了
    // 2.3 cause 失败的原因，成功时为null

    //回调接口
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了id为{}的消息", id);
        } else {
            log.info("交换机还未收到id为{}的消息，由于原因：{}", id, cause);
        }
    }


    //回退接口
    //在当消息传递过程中不可达目的地时将消息返回给生产者
    //只有不可达目的地时才进行回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        log.error("消息：{}，被交换机{}退回，退回原因：{}，路由key：{}",
                message.toString(), exchange, replyText, routingKey);
    }
}
