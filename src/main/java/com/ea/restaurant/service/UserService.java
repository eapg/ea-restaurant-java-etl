package com.ea.restaurant.service;

import com.ea.restaurant.entities.User;
import java.util.Optional;

public interface UserService extends GenericService<User, Long> {

  Optional<User> findEnabledByUsername(String username);
}
