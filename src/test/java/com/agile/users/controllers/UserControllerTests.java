package com.agile.users.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import com.agile.users.controllers.exceptions.handler.AppExceptionHandler;
import com.agile.users.entities.User;
import com.agile.users.entities.data.UserAccessLevel;
import com.agile.users.repositories.UserRepository;
import com.agile.users.services.UserService;
import com.agile.users.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {
  UserController.class,
  UserRepository.class,
  User.class,
  UserService.class,
  AppExceptionHandler.class
})
public class UserControllerTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserService userService;

  @MockBean
  private User user;

  @Autowired
  private UserController UserController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(UserController)
      .setControllerAdvice(new AppExceptionHandler())
      .build();
  }  

  @Test
  @WithMockUser(username="ADMIN")
  public void listAll() throws Exception {
    User user1 = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    User user2 = new User("Common", "common@domain.com", "common", UserAccessLevel.CASHIER);
    List<User> users = Arrays.asList(user1, user2);

    Mockito.when(this.userService.listAll()).thenReturn(users);
    RequestBuilder request = MockMvcRequestBuilders.get("/users/listAll");
    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertNotNull(result.getResponse().getContentAsString());
  }

  @WithMockUser(username="ADMIN")
  @Test
  public void listByExistentId() throws Exception {
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);
    user.setId(1L);

    Mockito.when(this.userService.findById(ArgumentMatchers.eq(user.getId()))).thenReturn(user);
    RequestBuilder request = MockMvcRequestBuilders.get("/users/1").contentType(MediaType.APPLICATION_JSON);
    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username="ADMIN")
  public void listByWrongId() throws Exception {
    Mockito.when(this.userService.findById(ArgumentMatchers.eq(6L))).thenThrow(NotFoundException.class);
    RequestBuilder request = MockMvcRequestBuilders.get("/users/6");

    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
  }

  @Test
  @WithMockUser(username="ADMIN")
  public void createUser() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    User user = new User("Agile", "agile@domain.com", "agile_admin", UserAccessLevel.ADMIN);

    Mockito.when(this.userService.create(ArgumentMatchers.eq(user))).thenReturn(user);
    RequestBuilder request = MockMvcRequestBuilders
      .post("/users/create")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user));

    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username="ADMIN")
  public void createUserWithNullValues() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    User user = new User();

    Mockito.when(this.userService.update(ArgumentMatchers.eq(user))).thenThrow(DataIntegrityViolationException.class);
    RequestBuilder request = MockMvcRequestBuilders
      .post("/users/create")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user));

    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  @WithMockUser(username="ADMIN")
  public void updateUser() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    User user = new User("NewAgile", "new_agile@domain.com", "new_password", UserAccessLevel.ADMIN);
    user.setId(5L);

    Mockito.when(this.userService.update(ArgumentMatchers.eq(user))).thenReturn(user);
    RequestBuilder request = MockMvcRequestBuilders
      .patch("/users/" + user.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user));

    MvcResult result = this.mvc.perform(request).andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertNotNull(result.getResponse().getContentAsString());
  }
}
