package com.xiaowo.provider.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.xiaowo.provider.cache.RedisCacheService;
import com.xiaowo.provider.model.Msg;
import com.xiaowo.provider.model.RabbitMQEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class MsgSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, InitializingBean {
    /**
     * Rabbit MQ 客户端
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RabbitmqCacheService rabbitmqCacheService;

    /**
     * 每2秒钟发送一个随机数字到队列
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void send() {
        this.sendMsgExample(new Msg(UUID.randomUUID().toString(),"2018","dsdsad"));
    }

        public void sendMsgExample(Msg msg) {
        CorrelationData correlationData = new CorrelationData();
//        rabbitmqCacheService.addCache(JSON.toJSONString(msg), correlationData);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("6000").build();
        rabbitTemplate.convertAndSend(RabbitMQEnum.EXAMPLE.getExchangeName() ,RabbitMQEnum.EXAMPLE.getRoutingKey(),properties, msg, correlationData);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        // ACK=true仅仅标示消息已被Broker接收到，并不表示已成功投放至消息队列中
        // ACK=false标示消息由于Broker处理错误，消息并未处理成功
        log.info("消息送达确认结果：{}", b);
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        // 当消息发送出去找不到对应路由队列时，将会把消息退回
        // 如果有任何一个路由队列接收投递消息成功，则不会退回消息
        String srt2 = new String();
        try {
            srt2 = new String(message.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("消息退回：{},原因:{},只有路由{},没有路由{}", srt2, s, s1, s2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 必须设置消息送达确认的方式
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
}
