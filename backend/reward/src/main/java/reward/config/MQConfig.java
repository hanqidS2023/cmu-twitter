package reward.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;


@Configuration
public class MQConfig {

    @Value("${rabbitmq.queue.name.newuser}")
    private String newUserqueue;

    @Value("${rabbitmq.queue.name.update.coins}")
    private String updateCoinsQueue;

    @Value("${rabbitmq.queue.name.update.points}")
    private String updatePointsQueue;

    @Value("${rabbitmq.queue.name.newuser.sub}")
    private String subNewUserqueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.exchange.fanout.name}")
    private String fanoutExchange;

    @Value("${rabbitmq.newuser.routing.key}")
    private String newUserRoutingKey;

    @Value("${rabbitmq.newfollower.routing.key}")
    private String newFollowerRoutingKey;

    @Value("${rabbitmq.newlike.routing.key}")
    private String newLikeRoutingKey;

    @Bean
    Queue newUserQueue() {
        return new Queue(newUserqueue);
    }

    @Bean
    Queue subNewUserQueue() {
        return new Queue(subNewUserqueue);
    }

    @Bean
    Queue updateCoinsQueue() {
        return new Queue(updateCoinsQueue);
    }

    @Bean
    Queue updatePointsQueue() {
        return new Queue(updatePointsQueue);
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
    Binding newUserBinding() {
        return BindingBuilder.bind(newUserQueue()).to(fanoutExchange());
    }

    @Bean
    Binding subNewUserBinding() {
        return BindingBuilder.bind(subNewUserQueue()).to(fanoutExchange());
    }

    @Bean
    Binding updateCoinsBinding() {
        return BindingBuilder.bind(updateCoinsQueue()).to(exchange()).with(newLikeRoutingKey);
    }

    @Bean
    Binding updatePointsBinding() {
        return BindingBuilder.bind(updatePointsQueue()).to(exchange()).with(newFollowerRoutingKey);
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

