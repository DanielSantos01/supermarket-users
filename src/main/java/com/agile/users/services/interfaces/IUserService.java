package com.agile.users.services.interfaces;

import java.util.List;

import com.agile.users.entities.User;

public interface IUserService {
  public List<User> listAll();
  public User findById(long id);
  public User create(User product);
  public User update(User user);
}
