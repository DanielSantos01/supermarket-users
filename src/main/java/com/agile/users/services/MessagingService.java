package com.agile.users.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agile.users.entities.User;
import com.agile.users.services.interfaces.IMessagingService;

@Service
public class MessagingService implements IMessagingService {
  @Value("${messaging.exchange-name}")
  private String exchangeName;

  @Value("${messaging.user-created-rk}")
  private String userCreatedRoutingKey;

  @Value("${messaging.user-updated-rk}")
  private String userUpdatedRoutingKey;

  @Autowired
  private RabbitTemplate template;

  public void notifyUserCreation(User user) {
    this.template.convertAndSend(this.exchangeName, this.userCreatedRoutingKey, user);
  }

  public void notifyUserUpdate(User user) {
    this.template.convertAndSend(this.exchangeName, this.userUpdatedRoutingKey, user);
  }
}
