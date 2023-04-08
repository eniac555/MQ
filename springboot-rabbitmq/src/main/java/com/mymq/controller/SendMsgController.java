package com.mymq.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 生产者：发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //开始发送消息  仅发消息，不设置过期时间
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA",
                "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB",
                "消息来自ttl为40s的队列：" + message);
    }


    //开始发消息并设置过期时间
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,
                        @PathVariable String ttlTime) {
        log.info("当前时间：{},发送一条时长{}ms的ttl信息给普通队列QC：{}",
                new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message,
                msg -> {
                    //设置发送消息时的延迟时长
                    msg.getMessageProperties().setExpiration(ttlTime);
                    return msg;
                });
    }
}
