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
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EncryptService encryptService;

  @Autowired
  private MessagingService messagingService;

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
    user.setPassword(this.encryptService.encode(user.getPassword()));
    User newUser = this.userRepository.save(user);
    this.messagingService.notifyUserCreation(newUser);
    return newUser;
  }

  public Optional<User> findByName(String name) {
    Optional<User> user = this.userRepository.findByName(name.toLowerCase());
    return user;
  }

  public User update(User user) {
    User old = this.findById(user.getId());
    Optional<User> previous = this.userRepository.findByName(user.getName());
    if (previous.isPresent() && previous.get().getId() != old.getId()) {
      String message = String.format("An user with name %s already exists", user.getName());
      throw new DuplicatedDocumentException(message);
    }

    user.setUpdatedAt(new Date());
    this.messagingService.notifyUserUpdate(user);
    return this.userRepository.save(user);
  }
}
