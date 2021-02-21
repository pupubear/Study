package com.lyx.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LYX
 * @date 2021/2/21 1:55
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String EXCHANGE_FANOUT = "boot_fanout_exchange";
    public static final String QUEUE_NAME = "boot_queue";
    public static final String QUEUE_FANOUT1 = "boot_fanout_queue1";
    public static final String QUEUE_FANOUT2 = "boot_fanout_queue2";
    public static final String QUEUE_DEFAULT = "queue";

    //1. 交换机
    // topic交换机
    @Bean("bootExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    // fanout交换机
    @Bean("fanoutExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.fanoutExchange(EXCHANGE_FANOUT).durable(true).build();
    }

    //2. Queue队列
    @Bean("bootQueue")
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    // fanout 需要绑定的Queue队列
    @Bean("fanoutQueue1")
    public Queue fanoutQueue1(){
        return QueueBuilder.durable(QUEUE_FANOUT1).build();
    }
    @Bean("fanoutQueue2")
    public Queue fanoutQueue2(){
        return QueueBuilder.durable(QUEUE_FANOUT2).build();
    }

    // 默认队列
    @Bean("deft")
    public Queue deft(){
        return QueueBuilder.durable(QUEUE_DEFAULT).build();
    }

    //3. 队列和交换机的绑定关系 binding
    /*
        1. 知道哪个队列
        2. 知道那个交换机
        3. routing key

        @@Qualifier("bean Name")：根据id给参数注入指定的bean
     */
    @Bean
    public Binding bindQueueExchangeTopic(@Qualifier("bootQueue") Queue queue,@Qualifier("bootExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    @Bean
    public Binding bindQueueExchangeFanout1(@Qualifier("fanoutExchange") Exchange exchange,
                                           @Qualifier("fanoutQueue1") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
    @Bean
    public Binding bindQueueExchangeFanout2(@Qualifier("fanoutExchange") Exchange exchange,
                                           @Qualifier("fanoutQueue2") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

}
