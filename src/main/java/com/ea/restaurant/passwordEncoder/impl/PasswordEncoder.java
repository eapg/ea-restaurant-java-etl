package com.ea.restaurant.passwordEncoder.impl;

import com.ea.restaurant.passwordEncoder.BaseEncoder;

public class PasswordEncoder {

  private final BaseEncoder passwordEncoder;

  public PasswordEncoder(BaseEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public boolean validatePassword(String password, String encodedPassword) {
    return this.passwordEncoder.validatePassword(password, encodedPassword);
  }

  public String encodePassword(String password) {
    return this.passwordEncoder.encodePassword(password);
  }
}
