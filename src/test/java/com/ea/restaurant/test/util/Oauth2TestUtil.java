package com.ea.restaurant.test.util;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.test.fixture.AppAccessTokenFixture;
import com.ea.restaurant.test.fixture.AppRefreshTokenFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class Oauth2TestUtil {

  public static MockedStatic<Oauth2Util> mockOauth2Util(
      AppClient appClient, AppClientScope appClientScope) throws JOSEException {
    var mockedOauth2Util = Mockito.mockStatic(Oauth2Util.class);

    Mockito.when(
            Oauth2Util.buildClientCredentialsToken(
                Mockito.eq(appClient),
                Mockito.eq(appClientScope),
                Mockito.eq(Oauth2Fixture.SECRET_KEY),
                Mockito.eq(Oauth2.TokenType.ACCESS_TOKEN)))
        .thenReturn(AppAccessTokenFixture.TOKEN);

    Mockito.when(
            Oauth2Util.buildClientCredentialsToken(
                Mockito.eq(appClient),
                Mockito.eq(appClientScope),
                Mockito.eq(Oauth2Fixture.SECRET_KEY),
                Mockito.eq(Oauth2.TokenType.REFRESH_TOKEN)))
        .thenReturn(AppRefreshTokenFixture.TOKEN);

    return mockedOauth2Util;
  }
}
