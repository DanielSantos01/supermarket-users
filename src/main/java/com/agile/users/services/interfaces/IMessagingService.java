package com.agile.users.services.interfaces;

import com.agile.users.entities.User;

public interface IMessagingService {
  public void send(User user, String to);
}
