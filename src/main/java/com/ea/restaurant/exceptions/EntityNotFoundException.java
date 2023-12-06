package com.ea.restaurant.exceptions;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException() {
    super("Entity was not found");
  }
}
