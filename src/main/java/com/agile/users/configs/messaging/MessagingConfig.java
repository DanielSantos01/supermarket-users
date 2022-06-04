package com.agile.users.configs.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
  @Value("${messaging.exchange-name}")
  private String exchangeName;

  @Value("${messaging.user-created-rk}")
  private String userCreatedRoutingKey;

  @Value("${messaging.user-updated-rk}")
  private String userUpdatedRoutingKey;

  @Value("${messaging.product-created-rk}")
  private String productCreatedRoutingKey;

  @Value("${messaging.product-updated-rk}")
  private String productUpdatedRoutingKey;

  @Value("${messaging.inventory-created-rk}")
  private String inventoryCreatedRoutingKey;

  @Value("${messaging.inventory-updated-rk}")
  private String inventoryUpdatedRoutingKey;

  @Value("${messaging.purchase-created-rk}")
  private String purchaseCreatedRoutingKey;


  @Bean
  @Qualifier("productConsumerUserCreated")
  public Queue productConsumerUserCreatedQueue() {
    return new Queue("productConsumerUserCreated");
  }

  @Bean
  @Qualifier("productConsumerUserUpdated")
  public Queue productConsumerUserUpdatedQueue() {
    return new Queue("productConsumerUserUpdated");
  }

  @Bean
  @Qualifier("paymentConsumerUserCreated")
  public Queue paymentConsumerUserCreatedQueue() {
    return new Queue("paymentConsumerUserCreated");
  }

  @Bean
  @Qualifier("paymentConsumerUserUpdated")
  public Queue paymentConsumerUserUpdatedQueue() {
    return new Queue("paymentConsumerUserUpdated");
  }


  @Bean
  @Qualifier("inventoryConsumerPurchaseCreated")
  public Queue inventoryConsumerPurchaseCreatedQueue() {
    return new Queue("inventoryConsumerPurchaseCreated");
  }

  @Bean
  @Qualifier("paymentConsumerPurchaseCreated")
  public Queue paymentConsumerPurchaseCreatedQueue() {
    return new Queue("paymentConsumerPurchaseCreated");
  }

  @Bean
  @Qualifier("productConsumerInventoryCreated")
  public Queue productConsumerInventoryCreatedQueue() {
    return new Queue("productConsumerInventoryCreated");
  }

  @Bean
  @Qualifier("productConsumerInventoryUpdated")
  public Queue productConsumerInventoryUpdated() {
    return new Queue("productConsumerInventoryUpdated");
  }

  @Bean
  @Qualifier("inventoryConsumerProductCreated")
  public Queue inventoryConsumerProductCreatedQueue() {
    return new Queue("inventoryConsumerProductCreated");
  }

  @Bean
  @Qualifier("inventoryConsumerProductUpdated")
  public Queue inventoryConsumerProductUpdatedQueue() {
    return new Queue("inventoryConsumerProductUpdated");
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(this.exchangeName);
  }

  @Bean
  public Binding productUserCreateBinding(@Qualifier("productConsumerUserCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.userCreatedRoutingKey);
  }

  @Bean
  public Binding productUserUpdateBinding(@Qualifier("productConsumerUserUpdated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.userUpdatedRoutingKey);
  }

  @Bean
  public Binding paymentUserCreationBinding(@Qualifier("paymentConsumerUserCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.userCreatedRoutingKey);
  }

  @Bean
  public Binding paymentUserUpdatedBinding(@Qualifier("paymentConsumerUserUpdated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.userUpdatedRoutingKey);
  }

  @Bean
  public Binding inventoryPurchaseCreatedBinding(@Qualifier("inventoryConsumerPurchaseCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.purchaseCreatedRoutingKey);
  }

  @Bean
  public Binding paymentPurchaseCreatedBinding(@Qualifier("paymentConsumerPurchaseCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.purchaseCreatedRoutingKey);
  }

  @Bean
  public Binding productInventoryCreatedBinding(@Qualifier("productConsumerInventoryCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.inventoryCreatedRoutingKey);
  }

  @Bean
  public Binding productInventoryUpdatedBinding(@Qualifier("productConsumerInventoryUpdated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.inventoryUpdatedRoutingKey);
  }

  @Bean
  public Binding inventoryProductCreatedBinding(@Qualifier("inventoryConsumerProductCreated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.productCreatedRoutingKey);
  }

  @Bean
  public Binding inventoryProductUpdatedBinding(@Qualifier("inventoryConsumerProductUpdated")Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(this.productUpdatedRoutingKey);
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
