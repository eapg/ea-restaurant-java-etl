package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClient;
import java.time.Instant;
import java.util.Optional;

public class AppClientFixture {

  public static final String TESTING_CLIENT_NAME = "TEST";
  public static final String TESTING_CLIENT_ID = "TEST01";
  public static final String TESTING_CLIENT_SECRET =
      "$2b$12$Z3rYJmr5ZuWBJ7h0IdnZ8OmxX3CyVQ3NuVsTkKI4cKx9hZmeyC7yW"; // TEST-CLIENT-SECRET
  public static final Integer ACCESS_TOKEN_EXPIRATION_TIME = 10;
  public static final Integer REFRESH_TOKEN_EXPIRATION_TIME = 10;
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static AppClient buildAppClient(AppClient appClient) {
    appClient.setId(appClient.getId());
    appClient.setClientName(
        Optional.ofNullable(appClient.getClientName()).orElse(TESTING_CLIENT_NAME));
    appClient.setClientId(Optional.ofNullable(appClient.getClientId()).orElse(TESTING_CLIENT_ID));
    appClient.setClientSecret(
        Optional.ofNullable(appClient.getClientSecret()).orElse(TESTING_CLIENT_SECRET));
    appClient.setAccessTokenExpirationTime(
        Optional.ofNullable(appClient.getAccessTokenExpirationTime())
            .orElse(ACCESS_TOKEN_EXPIRATION_TIME));
    appClient.setRefreshTokenExpirationTime(
        Optional.ofNullable(appClient.getRefreshTokenExpirationTime())
            .orElse(REFRESH_TOKEN_EXPIRATION_TIME));
    appClient.setEntityStatus(
        Optional.ofNullable(appClient.getEntityStatus()).orElse(ENTITY_STATUS));
    appClient.setCreatedDate(Optional.ofNullable(appClient.getCreatedDate()).orElse(CREATED_DATE));
    appClient.setCreatedBy(Optional.ofNullable(appClient.getCreatedBy()).orElse(CREATED_BY));
    appClient.setUpdatedBy(Optional.ofNullable(appClient.getUpdatedBy()).orElse(UPDATED_BY));
    appClient.setUpdatedDate(Optional.ofNullable(appClient.getUpdatedDate()).orElse(UPDATED_DATE));
    return appClient;
  }

  public static AppClient buildAppClient(
      Long id,
      String clientName,
      String clientId,
      String clientSecret,
      Integer accessTokenExpTime,
      Integer refreshTokenExpTime) {
    var appClientExample = new AppClient(id);
    appClientExample.setClientName(clientName);
    appClientExample.setClientId(clientId);
    appClientExample.setClientSecret(clientSecret);
    appClientExample.setAccessTokenExpirationTime(accessTokenExpTime);
    appClientExample.setRefreshTokenExpirationTime(refreshTokenExpTime);
    return buildAppClient(appClientExample);
  }

  public static AppClient buildAppClient(Long id) {
    var appClientExample = new AppClient(id);
    return buildAppClient(appClientExample);
  }
}
