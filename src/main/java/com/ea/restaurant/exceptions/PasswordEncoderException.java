package com.ea.restaurant.exceptions;

public class PasswordEncoderException extends RuntimeException {
  public PasswordEncoderException() {
    super("Unsupported encoder type");
  }
}
