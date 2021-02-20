package com.lyx.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author LYX
 * @date 2021/2/21 1:30
 */
public class FanoutListener1 implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //打印消息
        System.out.println("spring_fanout_queue_1===> " + new String(message.getBody()));
    }
}
