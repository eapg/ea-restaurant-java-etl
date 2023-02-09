package com.ea.restaurant.utils;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.utils.fixtures.AppClientFixture;
import com.ea.restaurant.utils.fixtures.AppClientScopeFixture;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
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
}
