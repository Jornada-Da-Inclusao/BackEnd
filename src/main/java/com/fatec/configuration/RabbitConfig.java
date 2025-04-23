package com.fatec.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        // Criando um ObjectMapper para configurar o conversor JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Retorna o conversor JSON para RabbitMQ
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
