package com.agile.users.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agile.users.entities.data.UserAccessLevel;

import org.junit.jupiter.api.Test;

public class UserEntityTests {
  
  @Test
  public void emptyUser() {
    User user = new User();
    assertEquals(UserAccessLevel.CASHIER, user.getAccessLevel());
    assertEquals(null, user.getName());
    assertEquals(null, user.getEmail());
    assertEquals(null, user.getPassword());

    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
  }

  @Test
  public void filledUser() {
    User user = new User("test", "test@domain.com", "123", UserAccessLevel.ADMIN);
    User newUser = new User("newName", "test@domain.com", "123", UserAccessLevel.ADMIN);

    assertEquals("test", user.getName());
    assertEquals("test@domain.com", user.getEmail());
    assertEquals(UserAccessLevel.ADMIN, user.getAccessLevel());

    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
    
    assertTrue(user.equals(user));
    assertFalse(user.equals(newUser));
  }
}
