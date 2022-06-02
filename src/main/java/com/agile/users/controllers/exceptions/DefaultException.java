package com.agile.users.controllers.exceptions;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultException implements Serializable {
  private static final long serialVersionUID = 1L;

  private Instant timestamp;

  private int status;

  private String error;

  private String message;

  private String path;

  public DefaultException(HttpStatus status, String error, String message, String path) {
    this.timestamp = Instant.now();
    this.status = status.value();
    this.error = error;
    this.message = message;
    this.path = path;
  }
}

