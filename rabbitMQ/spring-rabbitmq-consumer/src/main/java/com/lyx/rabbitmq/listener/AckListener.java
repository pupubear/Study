package com.lyx.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author LYX
 * @date 2021/2/21 15:30
 *
 *  Consumer ACK机制
 *  1. 设置手动签收 <rabbit:listener-container acknowledge="manual">
 *  2. 让监听器类实现 ChannelAwareMessageListener
 *  3. 如果消息处理成功，这调用channel的 basicAck() 接收
 *  4. 如果消息处理失败，这调用channel的 basicNack() 拒收，broker重新发送给consumer
 */

@Component
public class AckListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep(1000);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1. 接收转换消息
            System.out.println(new String(message.getBody()));
            //2. 处理业务逻辑
            System.out.println("处理业务逻辑...");
            //3. 手动签收
            int i = 3 / 0;
            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {
//            e.printStackTrace();
            //4.拒绝签收
            /*
            第三个参数：requeue：重回队列。如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
             */
            channel.basicNack(deliveryTag,true,true);
            //channel.basicReject(deliveryTag,true);
        }
    }
}
