package com.lyx.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author LYX
 * @date 2021/2/21 1:42
 */
public class TopicListenerWell implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //打印消息
        System.out.println("spring_topic_queue_well===> " + new String(message.getBody()));
    }
}
