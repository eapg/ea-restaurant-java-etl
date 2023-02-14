package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppAccessToken;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.Optional;

public class AppAccessTokenFixture {
  public static final String TOKEN = "ACCESS-TOKEN-FOR-TEST";
  public static final String EXPIRE_TOKEN = "ACCESS-TOKEN-EXPIRED";
  public static final Long TESTING_REFRESH_TOKEN_ID = 1L;

  public static AppAccessToken buildAppAccessToken(AppAccessToken appAccessToken) {
    appAccessToken.setId(appAccessToken.getId());
    appAccessToken.setToken(Optional.ofNullable(appAccessToken.getToken()).orElse(TOKEN));
    appAccessToken.setRefreshTokenId(
        Optional.ofNullable(appAccessToken.getRefreshTokenId()).orElse(TESTING_REFRESH_TOKEN_ID));
    return appAccessToken;
  }

  public static AppAccessToken buildAppAccessToken(Long id, String token, Long appRefreshTokenId) {
    var appAccessTokenExample = new AppAccessToken(id);
    appAccessTokenExample.setToken(token);
    appAccessTokenExample.setRefreshTokenId(appRefreshTokenId);
    return buildAppAccessToken(appAccessTokenExample);
  }

  public static AppAccessToken buildAppAccessToken(Long id) {
    var appAccessTokenExample = new AppAccessToken(id);
    return buildAppAccessToken(appAccessTokenExample);
  }

  public static String buildAccessToken(AppClient appClient, AppClientScope appClientScope)
      throws JOSEException {
    return Oauth2Util.buildClientCredentialsToken(
        appClient, appClientScope, Oauth2Fixture.SECRET_KEY, Oauth2.TokenType.ACCESS_TOKEN);
  }

  public static JWTClaimsSet buildTokenDecoded(String token)
      throws BadJOSEException, ParseException, JOSEException {

    return Oauth2Util.getTokenDecoded(token, Oauth2Fixture.SECRET_KEY);
  }
}
