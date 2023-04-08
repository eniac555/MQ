package com.mymq.controller;

import com.mymq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息的一方，用来验证高级发布确认
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发送消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {

        //正确发送
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message+"key1", correlationData);
        log.info("发送消息内容为：{}", message+"key1");

        //测试错误发送
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY+"2", message+"key12", correlationData2);
        log.info("发送消息内容为：{}", message+"key12");
    }



}
