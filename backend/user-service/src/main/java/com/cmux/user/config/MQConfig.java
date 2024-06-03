package com.cmux.user.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class MQConfig {
    
    @Value("${rabbitmq.queue.name.user.newicon}")
    private String newIconQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.exchange.fanout.name}")
    private String fanoutExchange;

    @Value("${rabbitmq.newicon.routing.key}")
    private String newIconRoutingKey;

    @Bean
    Queue userQueue() {
        return new Queue(newIconQueue);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean
    Binding userBinding() {
        return BindingBuilder.bind(userQueue()).to(exchange()).with(newIconRoutingKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
