package com.ea.restaurant.service;

import com.ea.restaurant.dtos.Oauth2TokenResponseDto;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public interface Oauth2Service {

  Oauth2TokenResponseDto loginClient(String clientId, String clientSecret)
      throws UnsupportedEncodingException, JOSEException;

  Oauth2TokenResponseDto refreshToken(
      String refreshToken, String expiredAccessToken, String clientId, String clientSecret)
      throws BadJOSEException, ParseException, JOSEException;
}
