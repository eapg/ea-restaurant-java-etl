package com.ea.restaurant.utils.fixtures;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppRefreshToken;
import java.util.Optional;

public class AppRefreshTokenFixture {

  public static final String TOKEN = "REFRESH-TOKEN-FOR-TEST";
  public static final Oauth2.GranType GRANT_TYPE = Oauth2.GranType.CLIENT_CREDENTIALS;
  public static final Long TESTING_APP_CLIENT_ID = 1L;

  public static AppRefreshToken buildAppRefreshToken(AppRefreshToken appRefreshToken) {
    appRefreshToken.setId(appRefreshToken.getId());
    appRefreshToken.setGrantType(
        Optional.ofNullable(appRefreshToken.getGrantType()).orElse(GRANT_TYPE));
    appRefreshToken.setToken(Optional.ofNullable(appRefreshToken.getToken()).orElse(TOKEN));
    appRefreshToken.setAppClientId(
        Optional.ofNullable(appRefreshToken.getAppClientId()).orElse(TESTING_APP_CLIENT_ID));
    return appRefreshToken;
  }

  public static AppRefreshToken buildAppRefreshToken(
      Long id, String token, Oauth2.GranType granType, Long appClientId) {
    var appRefreshTokenExample = new AppRefreshToken(id);
    appRefreshTokenExample.setAppClientId(appClientId);
    appRefreshTokenExample.setGrantType(granType);
    appRefreshTokenExample.setToken(token);
    return buildAppRefreshToken(appRefreshTokenExample);
  }

  public static AppRefreshToken buildAppRefreshToken(Long id) {
    var appRefreshTokenExample = new AppRefreshToken(id);
    return buildAppRefreshToken(appRefreshTokenExample);
  }
}
