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

  public User findById(long id) throws NotFoundException {
    Optional<User> user = this.userRepository.findById(id);
    if(!user.isPresent()) {
      String message = String.format("No user was found with id %d", id);
      throw new NotFoundException(message);
    }
    return user.get();
  }

  public User create(User user) throws DuplicatedDocumentException {
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

  public User update(User user) throws NotFoundException, DuplicatedDocumentException {
    User reference = this.findById(user.getId());
    Optional<User> previous = this.userRepository.findByName(user.getName());
    if (previous.isPresent() && previous.get().getId() != reference.getId()) {
      String message = String.format("An user with name %s already exists", user.getName());
      throw new DuplicatedDocumentException(message);
    }

    reference.setName(user.getName());
    reference.setAccessLevel(user.getAccessLevel());
    reference.setEmail(user.getEmail());
    reference.setUpdatedAt(new Date());
    this.userRepository.save(reference);
    this.messagingService.notifyUserUpdate(reference);
    return reference;
  }
}
