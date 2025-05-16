package com.example.orderservice.Config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Direct exchange for PaymentFailed events
    @Bean
    public DirectExchange paymentsExchange() {
        return new DirectExchange("payments");
    }

    // Topic exchange for logs
    @Bean
    public TopicExchange logExchange() {
        return new TopicExchange("log");
    }

}
