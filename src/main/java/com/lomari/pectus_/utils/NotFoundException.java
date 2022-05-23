package com.lomari.pectus_.utils;

public class NotFoundException extends RuntimeException {

  private String message;

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause, String message1) {
    super(message, cause);
    this.message = message1;
  }
}
