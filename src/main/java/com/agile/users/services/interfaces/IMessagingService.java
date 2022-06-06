package com.agile.users.services.interfaces;

import com.agile.users.entities.User;

public interface IMessagingService {
  public void notifyUserCreation(User user);
  public void notifyUserUpdate(User user);
}
