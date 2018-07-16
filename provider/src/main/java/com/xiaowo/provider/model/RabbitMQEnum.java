package com.xiaowo.provider.model;

import org.springframework.context.annotation.Configuration;

/**
 * @author EDZ
 */

public enum RabbitMQEnum {
    /**
     * example
     */
    EXAMPLE("exchange.example", "queue.example", "routing.example.*");
    private String exchangeName;
    private String queueName;
    private String routingKey;

    RabbitMQEnum(String exchangeName, String queueName, String routingKey) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
