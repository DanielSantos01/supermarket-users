package com.agile.users.configs.test;

import java.util.Arrays;

import com.agile.users.entities.User;
import com.agile.users.entities.data.UserAccessLevel;
import com.agile.users.repositories.UserRepository;
import com.agile.users.services.EncryptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EncryptService encryptServices;

  @Override
  public void run(String... args) throws Exception {
    this.createUsers();
  }

  private void createUsers() {
    /*
    User user1 = new User(
      "Agile",
      "agilesolutions@domain.com",
      this.encryptServices.getEncoder().encode("agile_admin"),
      UserAccessLevel.ADMIN
    );
    User user2 = new User(
      "Common",
      "comon.user@domain.com",
      this.encryptServices.getEncoder().encode("common"),
      UserAccessLevel.CASHIER
    );
    this.userRepository.saveAll(Arrays.asList(user1, user2));
    */
  }
}
