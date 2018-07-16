package com.xiaowo.consumer.consumer;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author EDZ
// */
@Component
@RabbitListener(queues = "hello")
public class Receiver {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("成功消费msg");
    }
}
