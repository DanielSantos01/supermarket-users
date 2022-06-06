package com.agile.users.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.agile.users.entities.User;
import com.agile.users.repositories.UserRepository;
import com.agile.users.services.exceptions.DuplicatedDocumentException;
import com.agile.users.services.exceptions.NotFoundException;
import com.agile.users.services.interfaces.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessagingService messagingService;

  @Value("${messaging.user-created-rk}")
  private String userCreatedRoutingKey;

  @Value("${messaging.user-updated-rk}")
  private String userUpdatedRoutingKey;

  public List<User> listAll() {
    List<User> users = this.userRepository.findAll();
    return users;
  }

  public User findById(long id) {
    User user = this.userRepository
      .findById(id)
      .orElseThrow(() -> {
        String message = String.format("No user was found with id %d", id);
        throw new NotFoundException(message);
      });
    return user;
  }

  public User create(User user) {
    Optional<User> previous = this.userRepository.findByName(user.getName());
    if (previous.isPresent()) {
      String message = String.format("An user with name %s already exists", user.getName());
      throw new DuplicatedDocumentException(message);
    }
    User newUser = this.userRepository.save(user);
    this.messagingService.send(newUser, this.userCreatedRoutingKey);
    return newUser;
  }

  public Optional<User> findByName(String name) {
    Optional<User> user = this.userRepository.findByName(name.toLowerCase());
    return user;
  }

  public User update(User user) {
    Optional<User> previous = this.userRepository.findByName(user.getName());
    if (previous.isPresent()) {
      String message = String.format("An user with name %s already exists", user.getName());
      throw new DuplicatedDocumentException(message);
    }
    User updatedUser = this.findById(user.getId());
    updatedUser.setName(user.getName());
    updatedUser.setEmail(user.getEmail());
    updatedUser.setAccessLevel(user.getAccessLevel());
    updatedUser.setUpdatedAt(new Date());

    this.messagingService.send(updatedUser, this.userUpdatedRoutingKey);
    return this.userRepository.save(updatedUser);
  }
}
