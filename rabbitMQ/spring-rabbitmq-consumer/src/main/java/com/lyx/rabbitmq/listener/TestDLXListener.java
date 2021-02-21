package com.lyx.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author LYX
 * @date 2021/2/21 15:30
 *  监听正常队列
 */

@Component
public class TestDLXListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("正常队列："+new String(message.getBody()));
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
    }
}
