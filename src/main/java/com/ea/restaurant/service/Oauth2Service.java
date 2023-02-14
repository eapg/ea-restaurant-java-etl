package com.ea.restaurant.service;

import com.ea.restaurant.dtos.LoginResponseDto;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public interface Oauth2Service {

  LoginResponseDto loginClient(String clientId, String clientSecret)
      throws UnsupportedEncodingException, JOSEException;

  LoginResponseDto refreshToken(
      String refreshToken, String expiredAccessToken, String clientId, String clientSecret)
      throws BadJOSEException, ParseException, JOSEException;
}
