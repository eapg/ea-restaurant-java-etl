package com.ea.restaurant.util;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.test.fixture.AppClientFixture;
import com.ea.restaurant.test.fixture.AppClientScopeFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import java.text.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Oauth2UtilTest {

  private String secretKey;
  private AppClient appClient;
  private AppClientScope appClientScope;

  @BeforeEach
  public void setUp() {
    secretKey = "thisIsTheSecretKeyForTest-Etl001";
    appClient = AppClientFixture.buildAppClient(1L);
    appClientScope = AppClientScopeFixture.buildAppClientScope(1L);
  }

  @Test
  public void whenTokenTypeAccessToken_ShouldCreateAccessToken()
      throws JOSEException, BadJOSEException, ParseException {

    var accessToken =
        Oauth2Util.buildClientCredentialsToken(
            this.appClient, this.appClientScope, secretKey, Oauth2.TokenType.ACCESS_TOKEN);
    var accessTokenDecoded = Oauth2Util.getTokenDecoded(accessToken, secretKey);
    Assertions.assertEquals(accessTokenDecoded.getClaim("clientName"), appClient.getClientName());
    Assertions.assertEquals(accessTokenDecoded.getClaim("scopes"), appClientScope.getScopes());
    Assertions.assertNotNull(accessToken);
  }

  @Test
  public void whenTokenTypeRefreshToken_ShouldCreateRefreshToken()
      throws JOSEException, BadJOSEException, ParseException {

    var refreshToken =
        Oauth2Util.buildClientCredentialsToken(
            this.appClient, this.appClientScope, secretKey, Oauth2.TokenType.REFRESH_TOKEN);
    var accessTokenDecoded = Oauth2Util.getTokenDecoded(refreshToken, secretKey);
    Assertions.assertEquals(accessTokenDecoded.getClaim("clientName"), appClient.getClientName());
    Assertions.assertEquals(accessTokenDecoded.getClaim("scopes"), appClientScope.getScopes());
    Assertions.assertNotNull(refreshToken);
  }

  @Test
  public void whenCreateAccessToken_ShouldValidateTokenSuccessfully()
      throws JOSEException, BadJOSEException, ParseException {

    var appClient = AppClientFixture.buildAppClient(1L);
    appClient.setAccessTokenExpirationTime(-100000);

    var accessToken =
        Oauth2Util.buildClientCredentialsToken(
            appClient, this.appClientScope, secretKey, Oauth2.TokenType.ACCESS_TOKEN);
    Assertions.assertThrows(
        BadJWTException.class, () -> Oauth2Util.validateToken(accessToken, secretKey));
    Assertions.assertNotNull(accessToken);
  }

  @Test
  public void whenEndpointIsProtected_shouldReturnTrue() {
    var endpointName = "insertMongoOrderStatusHistoriesFromPythonEtl";
    var isEndpointProtected = Oauth2Util.isEndpointProtected(endpointName);

    Assertions.assertTrue(isEndpointProtected);
  }

  @Test
  public void whenGetBearerToken_shouldReturnToken() {
    var expectedBasicToken = "VEVTVDAxOlRFU1QtQ0xJRU5ULVNFQ1JFVA";
    var metadata = Oauth2Fixture.buildMetaDataWithBearerToken(expectedBasicToken);
    var token = Oauth2Util.getBearerToken(metadata);
    Assertions.assertEquals(expectedBasicToken, token);
  }

  @Test
  public void whenValidateScopes_shouldNotThrowException()
      throws JOSEException, BadJOSEException, ParseException {
    var endpointName = "insertMongoOrderStatusHistoriesFromPythonEtl";
    var appClient = AppClientFixture.buildAppClient(1L);
    var appClientScopes = AppClientScopeFixture.buildAppClientScope(1L);
    var token =
        Oauth2Util.buildClientCredentialsToken(
            appClient, appClientScopes, secretKey, Oauth2.TokenType.ACCESS_TOKEN);
    Assertions.assertDoesNotThrow(
        () -> {
          Oauth2Util.validateScopes(token, secretKey, endpointName);
        });
  }

  @Test
  public void whenValidateEndpointProtection_shouldValidateEndpointProtection()
      throws BadJOSEException, ParseException, JOSEException {
    var endpointName = "insertMongoOrderStatusHistoriesFromPythonEtl";
    var appClient = AppClientFixture.buildAppClient(1L);
    var appClientScopes = AppClientScopeFixture.buildAppClientScope(1L);
    var token =
        Oauth2Util.buildClientCredentialsToken(
            appClient, appClientScopes, secretKey, Oauth2.TokenType.ACCESS_TOKEN);
    var metadata = Oauth2Fixture.buildMetaDataWithBearerToken(token);

    Oauth2Util.validateEndpointProtection(metadata, endpointName, secretKey);

    Assertions.assertDoesNotThrow(
        () -> Oauth2Util.validateEndpointProtection(metadata, endpointName, secretKey));

    Assertions.assertDoesNotThrow(() -> Oauth2Util.validateToken(token, secretKey));

    Assertions.assertDoesNotThrow(() -> Oauth2Util.validateScopes(token, secretKey, endpointName));
  }
}
