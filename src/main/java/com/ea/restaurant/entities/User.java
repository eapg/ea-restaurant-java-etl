package com.ea.restaurant.entities;

import com.ea.restaurant.constants.Roles;
import com.ea.restaurant.constants.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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

  @NotNull
  @Enumerated(EnumType.STRING)
  private Roles roles;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserType type;

  public User(Long id) {
    super(id);
  }
}
