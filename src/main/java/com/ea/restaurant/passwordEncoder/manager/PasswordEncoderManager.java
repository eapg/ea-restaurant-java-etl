package com.ea.restaurant.passwordEncoder.manager;

import com.ea.restaurant.constants.PasswordEncoderType;
import com.ea.restaurant.exceptions.PasswordEncoderException;
import com.ea.restaurant.passwordEncoder.impl.Base64Encoder;
import com.ea.restaurant.passwordEncoder.impl.BcryptEncoder;
import com.ea.restaurant.passwordEncoder.impl.PasswordEncoder;

public class PasswordEncoderManager {
  public static PasswordEncoder createPasswordEncoder(String passwordEncoderType) {
    if (PasswordEncoderType.BASE64.toString().equalsIgnoreCase(passwordEncoderType)) {
      return new PasswordEncoder(new Base64Encoder());
    }
    if (PasswordEncoderType.BCRYPT.toString().equalsIgnoreCase(passwordEncoderType)) {
      return new PasswordEncoder(new BcryptEncoder());
    }
    throw new PasswordEncoderException();
  }
}
