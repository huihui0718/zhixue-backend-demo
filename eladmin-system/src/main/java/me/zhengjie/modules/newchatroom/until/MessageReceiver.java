package me.zhengjie.modules.newchatroom.until;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.amqp.support.AmqpHeaders;

import java.util.Set;

@Component
public class MessageReceiver {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @RabbitListener(queues =  {"chatroom.2", "chatroom.3","chatroom.4" })
    public void receiveMessage(@Payload String message, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {
        System.out.println("来自队列:" + queueName+"收到消息为:" + message);

        }

}