package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.constants.RequestMetadataConstants;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import io.grpc.Metadata;
import java.util.Base64;

public class Oauth2Fixture {
  public static final String SECRET_KEY = "thisIsTheSecretKeyForTest-Etl001";
  public static final String TEST_CLIENT_NAME = "TEST-CLIENT-NAME";
  public static final String TEST_CLIENT_ID = "TEST01";
  public static final String TEST_WRONG_CLIENT_ID = "TEST02";
  public static final String TEST_CLIENT_SECRET = "TEST-CLIENT-SECRET";
  public static final String TEST_SCOPES = "READ,WRITE";
  public static final int TEST_EXPIRATION_TIME = 10;

  public static String buildAccessToken() throws JOSEException {
    var appClient = AppClientFixture.buildAppClient(1L);
    appClient.setAccessTokenExpirationTime(10);
    var appClientScope = AppClientScopeFixture.buildAppClientScope(1L);

    return Oauth2Util.buildClientCredentialsToken(
        appClient, appClientScope, Oauth2Fixture.SECRET_KEY, Oauth2.TokenType.ACCESS_TOKEN);
  }

  public static String buildExpiredAccessToken() throws JOSEException {
    var appClient = AppClientFixture.buildAppClient(1L);
    appClient.setAccessTokenExpirationTime(-100000);
    var appClientScope = AppClientScopeFixture.buildAppClientScope(1L);

    return Oauth2Util.buildClientCredentialsToken(
        appClient, appClientScope, Oauth2Fixture.SECRET_KEY, Oauth2.TokenType.ACCESS_TOKEN);
  }

  public static String buildRefreshToken() throws JOSEException {
    var appClient = AppClientFixture.buildAppClient(1L);
    var appClientScope = AppClientScopeFixture.buildAppClientScope(1L);
    return Oauth2Util.buildClientCredentialsToken(
        appClient, appClientScope, Oauth2Fixture.SECRET_KEY, Oauth2.TokenType.REFRESH_TOKEN);
  }

  public static Metadata buildMetadataWithBasicToken(String clientId, String clientSecret) {
    var credentials = String.format("%s:%s", clientId, clientSecret);
    var credentialsBytes = credentials.getBytes();
    var basicToken = Base64.getEncoder().withoutPadding().encodeToString(credentialsBytes);
    var metadata = new Metadata();
    metadata.put(
        Metadata.Key.of(
            RequestMetadataConstants.KEY_NAME_FOR_AUTHORIZATION_METADATA,
            Metadata.ASCII_STRING_MARSHALLER),
        "Basic" + basicToken);
    return metadata;
  }
}
