package com.agile.users.controllers;

import com.agile.users.entities.User;
import com.agile.users.services.exceptions.DuplicatedDocumentException;
import com.agile.users.services.exceptions.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/listAll")
  public ResponseEntity<List<User>> listAll() {
    // List<User> users = userServices.listAll();
    return ResponseEntity.ok(Arrays.asList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable long id) {
    try {
      // User user = this.userServices.findById(id);
      return ResponseEntity.ok(new User());
    } catch(NotFoundException e) {
      return ResponseEntity.ok(new User());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<User> create(@RequestBody User user) {
    try {
      // User newUser = this.userServices.create(user);
      return ResponseEntity.ok(new User());
    } catch(DuplicatedDocumentException e) {
      return ResponseEntity.ok(new User());
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
    try {
      // user.setId(id);
      // User updatedUser = this.userServices.update(user);
      return ResponseEntity.ok(new User());
    } catch(DuplicatedDocumentException e) {
      return ResponseEntity.ok(new User());
    } catch(NotFoundException e) {
      return ResponseEntity.ok(new User());
    }
  }
}
