package com.ea.restaurant.passwordEncoder;

public interface BaseEncoder {
  boolean validatePassword(String password, String encodedPassword);

  String encodePassword(String password);
}
