package com.ea.restaurant.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class User extends BaseEntity {
  private String name;
  private String lastName;
  private String username;
  private String password;
  private String roles;
  private String type;

  public User(long id) {
    super(id);
  }
}
