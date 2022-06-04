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

  @Autowired
  private RabbitTemplate template;

  public void send(User user, String to) {
    this.template.convertAndSend(this.exchangeName, to, user);
  }
}
