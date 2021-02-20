package com.example.springbootconsumer.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LYX
 * @date 2021/2/21 2:29
 */
@Component
public class RabbitMQListener {

    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message) {
        System.out.println("boot_queue==> " + new String(message.getBody()));
    }
}
