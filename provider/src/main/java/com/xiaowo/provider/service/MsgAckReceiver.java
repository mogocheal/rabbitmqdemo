package com.xiaowo.provider.service;

import com.rabbitmq.client.Channel;
import com.xiaowo.provider.model.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 带消息确认的ACK消费者
 *
 * @author laoyuan.me
 */
@Slf4j
@Service
public class MsgAckReceiver {

    @Autowired
    private RabbitmqCacheService rabbitmqCacheService;

    /**
     * 消息队列，只接收消息内容
     */
    @RabbitListener(queues = "queue.example")
    public void receivMsg(Msg msg, Message message, Channel channel) {
        // 确认消息已经消费成功
        try {
            // 确认成功
//            int i= 50/0;
            System.out.println(message.getMessageProperties().getDeliveryTag());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消费者消费消息：" + msg + ",");
            String id = message.getMessageProperties().getHeaders().get("spring_listener_return_correlation").toString();
            rabbitmqCacheService.del(id);
        } catch (Exception e) {
            log.info("消费失败,抛弃此条消息，原因：{}", e.getMessage());
            try {
                // 确认失败 重新放入队列
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                //确认失败 删除消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
