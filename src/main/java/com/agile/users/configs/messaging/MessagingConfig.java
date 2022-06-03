package com.agile.users.configs.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
  @Value("${messaging.exchange-name}")
  private String EXCHANGE_NAME;

  @Value("${messaging.user-created-queue}")
  private String USER_CREATED_QUEUE;

  @Value("${messaging.user-created-queue-rk}")
  private String USER_CREATED_QUEUE_RK;

  @Value("${messaging.user-updated-queue}")
  private String USER_UPDATED_QUEUE;

  @Value("${messaging.user-created-queue-rk}")
  private String USER_UPDATED_QUEUE_RK;

  @Bean
  public Queue creationQueue() {
    return new Queue(USER_CREATED_QUEUE);
  }

  @Bean
  public Queue updateQueue() {
    return new Queue(USER_UPDATED_QUEUE);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  public Binding creationBinding(Queue creationQueue, DirectExchange exchange) {
    return BindingBuilder.bind(creationQueue).to(exchange).with(USER_CREATED_QUEUE_RK);
  }

  @Bean
  public Binding updateBinding(Queue updateQueue, DirectExchange exchange) {
    return BindingBuilder.bind(updateQueue).to(exchange).with(USER_UPDATED_QUEUE_RK);
  }

  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public AmqpTemplate template(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }  
}
