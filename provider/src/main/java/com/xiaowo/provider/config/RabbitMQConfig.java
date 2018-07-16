package com.xiaowo.provider.config;

import com.xiaowo.provider.model.RabbitMQEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig implements InitializingBean {

    /**
     * MQ管理代理
     */
    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * bean加载以后调用
     */
    @Override
    public void afterPropertiesSet() {

        voidInit(RabbitMQEnum.EXAMPLE);
    }


    /**
     * 初始化信息
     *
     * @param mqEnum
     */
    public void voidInit(RabbitMQEnum mqEnum) {
        Exchange exchange = new TopicExchange(mqEnum.getExchangeName());
        Queue queue = new Queue(mqEnum.getQueueName());
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl", 60000);
        Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchange.getName(), mqEnum.getRoutingKey(), args);
        amqpAdmin.declareBinding(binding);
    }

    /**
     * 消息json格式发送
     *
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
