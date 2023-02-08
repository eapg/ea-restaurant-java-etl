package com.ea.restaurant.exceptions;

public class BcryptException extends RuntimeException {
  public BcryptException() {
    super("Invalid credentials");
  }
}
