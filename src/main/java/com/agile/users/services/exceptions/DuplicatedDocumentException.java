package com.agile.users.services.exceptions;

public class DuplicatedDocumentException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DuplicatedDocumentException() {
    super();
  }
  public DuplicatedDocumentException(String message) {
    super(message);
  }
}
