package com.ea.restaurant.service;

import com.ea.restaurant.dtos.LoginResponseDto;
import com.nimbusds.jose.JOSEException;
import java.io.UnsupportedEncodingException;

public interface Oauth2Service {

  LoginResponseDto loginClient(String clientId, String clientSecret)
      throws UnsupportedEncodingException, JOSEException;
}
