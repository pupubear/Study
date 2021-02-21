package com.lyx.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

///**
// * @author LYX
// * @date 2021/2/21 15:30
// *  监听死信队列
// */
//
//@Component
//public class DLXListener implements ChannelAwareMessageListener {
//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        System.out.println("死信队列监听器："+new String(message.getBody()));
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
//    }
//}
