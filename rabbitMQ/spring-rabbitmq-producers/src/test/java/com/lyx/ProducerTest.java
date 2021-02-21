package com.lyx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author LYX
 * @date 2021/2/21 1:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    //1.注入 RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_queue", "hello world spring and rabbitMQ");

    }

    /**
     * 发送 fanout 消息
     */
    @Test
    public void testFanout() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "spring fanout...");

    }

    /**
     * 发送 topic 消息
     */
    @Test
    public void testTopic() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring_topic_exchange", "itcast.1.2.3", "spring topic...");

    }

    /**
     * 测试消息可靠性
     * 确认模式
     * 步骤：
     * 1. 确认模式开启；ConnectionFactory 中开启publisher-confirm="true"
     * 2. 在 rabbitTemplate 定义 ConfirmCallback 回调函数
     */
    @Test
    public void testConfirm() throws InterruptedException {
        /**
         * 参数信息：
         *      * 1. correlationData：相关配置信息
         *      * 2. ack：exchange交换机是否成功收到消息 true：成功 false：失败
         *      * 3. cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                System.out.println("接受成功！");
            } else {
                System.out.println("接受失败！失败原因：" + cause);
                //做一些处理，让消息再次发送
            }

        });
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "hello~");
        Thread.sleep(2000);
    }


    /**
     * 回退模式：当消息发送给Exchange后，Exchange路由到Queue失败才会执行 ReturnCallback
     * 步骤：
     * 1. 开启回退模式：publisher-returns="true"
     * 2. 设置对应的 ReturnCallback
     * 3. 设置一个Exchange
     * 1. 如果消息没有路由到Queue，则丢弃消息（默认）
     * 2. 如果消息没有路由到Queue，返回给消息的发送方 ReturnCallback
     * <p>
     * tip: 关于无法执行ReturnCallback
     * 网上查阅了半天没找到解决问题的，后来想了一下，我是在测试方法中进行测试，当测试方法结束，
     * rabbitmq相关的资源也就关闭了，虽然我们的消息发送出去，但异步的ReturnCallback却由于资源关闭而出现了上面的问题
     *          上面的testConfirm()同理
     */
    @Test
    public void testReturn() throws InterruptedException {
        // 设置交换机处理消息的模式
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message   消息对象
             * @param replyCode 错误码
             * @param replyText 错误信息
             * @param exchange  交换机
             * @param routingKey 路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText,
                                        String exchange, String routingKey) {
                System.out.println("return 执行了");

                System.out.println(message);
                System.out.println(replyCode);
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);

                //处理
            }
        });

        for (int i = 0; i < 1000; i++) {
            rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", i+"==>hello~");
        }

        Thread.sleep(2000);
    }

    /**
     * TTL:过期时间
     *  1. 队列统一过期
     *
     *  2. 消息单独过期
     *
     *
     * 如果设置了消息的过期时间，也设置了队列的过期时间，它以时间短的为准。
     * 队列过期后，会将队列所有消息全部移除。
     * 消息过期后，只有消息在队列顶端，才会判断其是否过期(移除掉)
     *
     */
    @Test
    public void testTTL() throws InterruptedException {
        /*for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("test_exchange_ttl","ttl.haha","message ttl...");
        }*/
        //2. 消息单独过期

        // 消息的后处理对象，设置一些消息的处理信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //1.设置message的信息
                message.getMessageProperties().setExpiration("5000");//消息的过期时间

                return message;
            }
        };
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("test_exchange_ttl","ttl.haha","message ttl...",messagePostProcessor);
        }
    }

    /**
     * 发送测试死信消息
     *  1. 过期时间
     *  2. 长度限制
     *  3. 消息拒收
     */
    @Test
    public void testDXL() {
        rabbitTemplate.convertAndSend("test_exchange_dlx","test.dlx.#","我是一条消息，我会死吗？");
    }

    /**
     * order 订单 延迟队列
     */
    @Test
    public void testOrder() {
        rabbitTemplate.convertAndSend("order_queue","order.order","用户已下单...");
    }
}
