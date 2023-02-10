package com.ea.restaurant.utils.fixtures;

import com.ea.restaurant.entities.AppAccessToken;
import java.util.Optional;

public class AppAccessTokenFixture {
  public static final String TOKEN = "ACCESS-TOKEN-FOR-TEST";
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
}
