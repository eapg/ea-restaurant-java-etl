package com.ea.restaurant.constants;

public enum UserType {
  INTERNAL(1L),
  EXTERNAL(2L);

  private final Long id;

  UserType(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
