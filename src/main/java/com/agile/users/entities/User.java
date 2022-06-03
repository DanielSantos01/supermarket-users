package com.agile.users.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.agile.users.entities.data.UserAccessLevel;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Integer accessLevel;

  @Column(nullable = false)
  private final Instant createdAt = Instant.now();

  @Column(nullable = false)
  private Instant updatedAt = Instant.now();

  public User() {
    this.accessLevel = UserAccessLevel.CASHIER.getCode();
  }
  public User(String name, String email, String password, UserAccessLevel accessLevel) {
    this.name = name.toLowerCase();
    this.email = email;
    this.password = password;
    this.setAccessLevel(accessLevel);
  }

  public UserAccessLevel getAccessLevel() {
    return UserAccessLevel.valueOf(this.accessLevel);
  }

  public void setName(String name) {
    this.name = name.toLowerCase();
  }

  public void setAccessLevel(UserAccessLevel accessLevel) {
    this.accessLevel = accessLevel.getCode();
  }

  public Date getUpdatedAt() {
    return Date.from(this.updatedAt);
  }

  public Date getCreatedAt() {
    return Date.from(this.createdAt);
  }
}
