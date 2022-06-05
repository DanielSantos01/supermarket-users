/*
package com.agile.users.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.agile.users.entities.User;
import com.agile.users.entities.data.UserAccessLevel;
import com.agile.users.repositories.UserRepository;
import com.agile.users.services.exceptions.DuplicatedDocumentException;
import com.agile.users.services.exceptions.NotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest(classes = {UserRepository.class, UserService.class})
public class UserServiceTests {
  @MockBean
  private UserRepository userRepository;

  @MockBean
  private MessagingService messagingService;

  @MockBean
  private EncryptService encryptService;

  @Autowired
  private UserService userService;

  @Test
  public void userCreation() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    Optional<User> empty = Optional.empty();
  
    Mockito.when(userRepository.findByName(ArgumentMatchers.eq(user.getName()))).thenReturn(empty);
    Mockito.when(userRepository.save(ArgumentMatchers.eq(user))).thenReturn(user);

    User response = this.userService.create(user);
    assertNotNull(response);
    assertEquals(response, user);
  }

  @Test
  public void emptyUserCreation() {
    User user = new User();
    Mockito
      .when(this.userRepository.save(ArgumentMatchers.eq(user)))
      .thenThrow(DataIntegrityViolationException.class);

    assertThrows(DataIntegrityViolationException.class, () -> this.userService.create(user));
  }

  @Test
  public void duplicatedName() {
    User user1 = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User user2 = new User("Agile", "agile@otherdomain.com", "agile_admin", UserAccessLevel.ADMIN);

    Optional<User> empty = Optional.empty();
    Optional<User> filled = Optional.of(user2);

    Mockito.when(this.userRepository.findByName(ArgumentMatchers.eq(user1.getName()))).thenReturn(empty);
    Mockito.when(this.userRepository.save(ArgumentMatchers.eq(user1))).thenReturn(user1);

    User response1 = this.userService.create(user1);
    assertEquals(user1, response1);

    Mockito.when(this.userRepository.findByName(ArgumentMatchers.eq(user2.getName()))).thenReturn(filled);
    
    assertThrows(DuplicatedDocumentException.class, () ->  this.userService.create(user2));
  }

  @Test
  public void findByExistentId() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
     
    Optional<User> filled = Optional.of(user);
    Mockito.when(this.userRepository.findById(ArgumentMatchers.eq(user.getId()))).thenReturn(filled);

    User response = this.userService.findById(user.getId());

    assertNotNull(response);
    assertEquals(user, response);
  }

  @Test
  public void findByWrongId() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
     
    Optional<User> empty = Optional.empty();
    Mockito.when(this.userRepository.findById(ArgumentMatchers.eq(user.getId()))).thenReturn(empty);

    assertThrows(NotFoundException.class, () -> this.userService.findById(user.getId()));
  }

  @Test
  public void findAll() {
    User user1 = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User user2 = new User("Agile_S", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    List<User> collection = Arrays.asList(user1, user2);
 
    Mockito.when(this.userRepository.findAll()).thenReturn(collection);

    List<User> users = this.userService.listAll();

    assertTrue(users instanceof List<?>);
    assertEquals(user1, users.get(0));
    assertEquals(user2, users.get(1));
    assertEquals(2, users.size());
  }

  @Test
  public void findByName() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    this.userService.create(user);

    Optional<User> filledResponse = Optional.of(user);

    Mockito.when(this.userRepository.findByName(ArgumentMatchers.eq("agile"))).thenReturn(filledResponse);
    Mockito.when(this.userRepository.findByName(ArgumentMatchers.eq("Agile"))).thenReturn(filledResponse);

    Optional<User> response1 = this.userService.findByName("Agile");
    Optional<User> response2 = this.userService.findByName("agile");

    assertTrue(response1.isPresent());
    assertEquals(user, response1.get());

    assertTrue(response2.isPresent());
    assertEquals(user, response2.get());
  }

  @Test
  public void update() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    Optional<User> filled = Optional.of(user);
 
    Mockito.when(this.userRepository.findById(ArgumentMatchers.eq(user.getId()))).thenReturn(filled);

    user.setName("newName");
    Mockito.when(this.userRepository.save(ArgumentMatchers.eq(user))).thenReturn(user);
    User updateResponse = this.userService.update(user);

    assertEquals("newname", updateResponse.getName());
    assertEquals("agile@domain.com", updateResponse.getEmail());
  }
}
*/