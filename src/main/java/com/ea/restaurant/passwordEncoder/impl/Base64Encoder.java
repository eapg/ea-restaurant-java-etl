package com.ea.restaurant.passwordEncoder.impl;

import com.ea.restaurant.passwordEncoder.BaseEncoder;
import java.util.Base64;
import java.util.Objects;

public class Base64Encoder implements BaseEncoder {
  @Override
  public boolean validatePassword(String password, String encodedPassword) {
    var decodedPasswordBytes = Base64.getDecoder().decode(encodedPassword);
    var decodedPassword = new String(decodedPasswordBytes);
    return Objects.equals(password, decodedPassword);
  }

  @Override
  public String encodePassword(String password) {
    return Base64.getEncoder().encodeToString(password.getBytes());
  }
}
