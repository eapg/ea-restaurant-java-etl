package com.ea.restaurant.utils.fixtures;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClientScope;
import java.time.Instant;
import java.util.Optional;

public class AppClientScopeFixture {

  public static final Long TESTING_APP_CLIENT_ID = 1L;
  public static final String TESTING_SCOPES = "READ,WRITE";
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static AppClientScope buildAppClientScope(AppClientScope appClientScope) {
    appClientScope.setId(appClientScope.getId());
    appClientScope.setAppClientId(
        Optional.ofNullable(appClientScope.getAppClientId()).orElse(TESTING_APP_CLIENT_ID));
    appClientScope.setScopes(
        Optional.ofNullable(appClientScope.getScopes()).orElse(TESTING_SCOPES));
    appClientScope.setEntityStatus(
        Optional.ofNullable(appClientScope.getEntityStatus()).orElse(ENTITY_STATUS));
    appClientScope.setCreatedDate(
        Optional.ofNullable(appClientScope.getCreatedDate()).orElse(CREATED_DATE));
    appClientScope.setCreatedBy(
        Optional.ofNullable(appClientScope.getCreatedBy()).orElse(CREATED_BY));
    appClientScope.setUpdatedBy(
        Optional.ofNullable(appClientScope.getUpdatedBy()).orElse(UPDATED_BY));
    appClientScope.setUpdatedDate(
        Optional.ofNullable(appClientScope.getUpdatedDate()).orElse(UPDATED_DATE));
    return appClientScope;
  }

  public static AppClientScope buildAppClientScope(Long id, Long appClientId, String scopes) {
    var appClientScopeExample = new AppClientScope(id);
    appClientScopeExample.setAppClientId(appClientId);
    appClientScopeExample.setScopes(scopes);
    return buildAppClientScope(appClientScopeExample);
  }

  public static AppClientScope buildAppClientScope(Long id) {
    var appClientScopeExample = new AppClientScope(id);
    return buildAppClientScope(appClientScopeExample);
  }
}
