package com.ea.restaurant.config;

import com.ea.restaurant.passwordEncoder.impl.PasswordEncoder;
import com.ea.restaurant.passwordEncoder.manager.PasswordEncoderManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

  @Value("${security.password.encoding.type}")
  private String passwordEncoderType;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderManager.createPasswordEncoder(passwordEncoderType);
  }
}
