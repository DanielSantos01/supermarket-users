package com.agile.users.controllers.exceptions.handler;

import javax.servlet.http.HttpServletRequest;

import com.agile.users.controllers.exceptions.DefaultException;
import com.agile.users.services.exceptions.DuplicatedDocumentException;
import com.agile.users.services.exceptions.NotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
  private DefaultException getException(String error, String message, String path, HttpStatus status) {
    return new DefaultException(status, error, message, path);
  }
  
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<DefaultException> notFoundHandler(NotFoundException ex, HttpServletRequest req) {
    DefaultException exception = this.getException(
      "Not Found",
      ex.getMessage(),
      req.getRequestURI(),
      HttpStatus.NOT_FOUND
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<DefaultException> invalidBodyHandler(DataIntegrityViolationException ex, HttpServletRequest req) {
    DefaultException exception = this.getException(
      "Bad Request",
      ex.getMessage(),
      req.getRequestURI(),
      HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<DefaultException> emptyBodyHandler(HttpMessageNotReadableException ex, HttpServletRequest req) {
    DefaultException exception = this.getException(
      "Bad Request",
      ex.getMessage(),
      req.getRequestURI(),
      HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
  }

  @ExceptionHandler(DuplicatedDocumentException.class)
  public ResponseEntity<DefaultException> duplicatedDocumentHandler(DuplicatedDocumentException ex, HttpServletRequest req) {
    DefaultException exception = this.getException(
      "Bad Request",
      ex.getMessage(),
      req.getRequestURI(),
      HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
  }
}
