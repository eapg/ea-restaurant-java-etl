package com.ea.restaurant.exceptions;

public class WrongCredentialsException extends RuntimeException {
  public WrongCredentialsException() {
    super("Wrong Credentials");
  }
}
