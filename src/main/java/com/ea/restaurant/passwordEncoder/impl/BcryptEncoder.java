package com.ea.restaurant.passwordEncoder.impl;

import com.ea.restaurant.passwordEncoder.BaseEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptEncoder implements BaseEncoder {
  @Override
  public boolean validatePassword(String password, String encodedPassword) {
    return BCrypt.checkpw(password, encodedPassword);
  }

  @Override
  public String encodePassword(String password) {
    return BCrypt.hashpw(password.getBytes(), BCrypt.gensalt());
  }
}
