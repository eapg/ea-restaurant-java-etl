package com.ea.restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @NotNull private String name;
  @NotNull private String lastName;
  @NotNull private String username;
  @NotNull private String password;
  @NotNull private String roles;
  @NotNull private String type;

  public User(Long id) {
    super(id);
  }
}
