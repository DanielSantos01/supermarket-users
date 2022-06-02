package com.agile.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.agile.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
  public Optional<User> findByName(String name);
}
