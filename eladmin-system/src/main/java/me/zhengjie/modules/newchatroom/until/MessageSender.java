package me.zhengjie.modules.newchatroom.until;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchangeName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}