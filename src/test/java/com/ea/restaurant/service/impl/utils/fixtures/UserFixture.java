package com.ea.restaurant.service.impl.utils.fixtures;

import com.ea.restaurant.constants.Roles;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.constants.UserType;
import com.ea.restaurant.entities.User;
import java.time.Instant;
import java.util.Optional;

public final class UserFixture {

  public static final String TESTING_USER_NAME = "juan";
  public static final String TESTING_LASTNAME = "perez";
  public static final String TESTING_USER_USERNAME = "jp001";
  public static final String PASSWORD = "1234";
  public static final Roles ROLES = Roles.ADMINISTRATOR;
  public static final UserType TYPE = UserType.INTERNAL;
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static User buildUser(User user) {
    user.setId(user.getId());
    user.setName(Optional.ofNullable(user.getName()).orElse(TESTING_USER_NAME));
    user.setLastName(Optional.ofNullable(user.getLastName()).orElse(TESTING_LASTNAME));
    user.setUsername(Optional.ofNullable(user.getUsername()).orElse(TESTING_USER_USERNAME));
    user.setPassword(Optional.ofNullable(user.getPassword()).orElse(PASSWORD));
    user.setRoles(Optional.ofNullable(user.getRoles()).orElse(ROLES));
    user.setType(Optional.ofNullable(user.getType()).orElse(TYPE));
    user.setEntityStatus(Optional.ofNullable(user.getEntityStatus()).orElse(ENTITY_STATUS));
    user.setCreatedDate(Optional.ofNullable(user.getCreatedDate()).orElse(CREATED_DATE));
    user.setCreatedBy(Optional.ofNullable(user.getCreatedBy()).orElse(CREATED_BY));
    user.setUpdatedBy(Optional.ofNullable(user.getUpdatedBy()).orElse(UPDATED_BY));
    user.setUpdatedDate(Optional.ofNullable(user.getUpdatedDate()).orElse(UPDATED_DATE));
    return user;
  }

  public static User buildUser(
      Long id, String name, String lastName, String username, String password) {
    var userExample = new User(id);
    userExample.setName(name);
    userExample.setLastName(lastName);
    userExample.setUsername(username);
    userExample.setPassword(password);
    return buildUser(userExample);
  }

  public static User buildUser(Long id) {
    var userExample = new User(id);
    return buildUser(userExample);
  }

  public static User buildUser() {
    var userExample = new User();
    return buildUser(userExample);
  }
}
