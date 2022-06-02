package com.agile.users.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.agile.users.entities.User;
import com.agile.users.entities.data.UserAccessLevel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTests {
  @Autowired
  private UserRepository userRepository;

  @Test
  public void userCreation() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User response = this.userRepository.save(user);

    assertNotNull(response);
    assertTrue(user.equals(response));
  }

  @Test
  public void emptyUserCreation() {
    User user = new User();
    assertThrows(DataIntegrityViolationException.class, () -> this.userRepository.save(user));
  }

  @Test
  public void duplicatedName() {
    User user1 = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User response1 = this.userRepository.save(user1);
    assertEquals(user1, response1);

    User user2 = new User("Agile", "agile@otherdomain.com", "agile_admin", UserAccessLevel.ADMIN);
    assertThrows(DataIntegrityViolationException.class, () ->  this.userRepository.save(user2));
  }

  @Test
  public void requiredParamsWithNullValues() {
    assertThrows(
      NullPointerException.class,
      () -> new User(null, null, null, null)
    );

    assertThrows(
      NullPointerException.class,
      () -> new User(null, null, null, UserAccessLevel.ADMIN)
    );

    assertThrows(
      NullPointerException.class,
      () -> new User(null, null, "password", UserAccessLevel.ADMIN)
    );
  }

  @Test
  public void findByExistentId() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    this.userRepository.save(user);
    Optional<User> response = this.userRepository.findById(user.getId());

    assertTrue(response.isPresent());
    assertEquals(user, response.get());
  }

  @Test
  public void findByWrongId() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    this.userRepository.save(user);
    Optional<User> response = this.userRepository.findById(1000L);

    assertTrue(!response.isPresent());
    assertThrows(NoSuchElementException.class, () -> response.get());
  }

  @Test
  public void findAll() {
    User user1 = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User user2 = new User("Agile_S", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    this.userRepository.saveAll(Arrays.asList(user1, user2));
    List<User> users = this.userRepository.findAll();

    assertTrue(users instanceof List<?>);
    assertEquals(user1, users.get(0));
    assertEquals(user2, users.get(1));
    assertEquals(2, users.size());
  }

  @Test
  public void findByName() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    this.userRepository.save(user);

    Optional<User> response1 = this.userRepository.findByName("Agile");
    Optional<User> response2 = this.userRepository.findByName("agile");

    assertFalse(response1.isPresent());
    assertThrows(NoSuchElementException.class, () -> response1.get());

    assertTrue(response2.isPresent());
    assertEquals(user, response2.get());
  }

  @Test
  public void update() {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User creationResponse = this.userRepository.save(user);

    assertEquals("agile", creationResponse.getName());
    assertEquals("agile@domain.com", creationResponse.getEmail());

    creationResponse.setName("newName");
    User updateResponse = this.userRepository.save(creationResponse);

    assertEquals("newname", updateResponse.getName());
    assertEquals("agile@domain.com", updateResponse.getEmail());
  }
}
