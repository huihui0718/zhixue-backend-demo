//package me.zhengjie.config;
//
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Autowired
//    private AmqpAdmin amqpAdmin;
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("exchange.chatroom");
//    }
//
//    @Bean
//    public Queue queue() {
//        return new Queue("chatroom.queue");
//    }
//
//    @Bean
//    public Queue queue3() {
//        return new Queue("chatroom.3");
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        return container;
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(directExchange()).with("chatroom.queue");
//    }
//
//    @Bean
//    public Binding binding3() {
//        return BindingBuilder.bind(queue3()).to(directExchange()).with("chatroom.3");
//    }
//}
