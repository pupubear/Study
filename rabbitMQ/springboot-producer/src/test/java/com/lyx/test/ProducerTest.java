package com.lyx.test;

import com.lyx.rabbitmq.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Scanner;

/**
 * @author LYX
 * @date 2021/2/21 2:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

    //1. 注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.#","boot MQ hello~~~");
    }
}
