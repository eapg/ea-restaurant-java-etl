package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.entities.AppRefreshToken;
import com.ea.restaurant.repository.AppAccessTokenRepository;
import com.ea.restaurant.repository.AppClientRepository;
import com.ea.restaurant.repository.AppClientScopeRepository;
import com.ea.restaurant.repository.AppRefreshTokenRepository;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.utils.Oauth2Util;
import com.ea.restaurant.utils.fixtures.AppAccessTokenFixture;
import com.ea.restaurant.utils.fixtures.AppClientFixture;
import com.ea.restaurant.utils.fixtures.AppClientScopeFixture;
import com.ea.restaurant.utils.fixtures.AppRefreshTokenFixture;
import com.ea.restaurant.utils.fixtures.Oauth2Fixture;
import com.nimbusds.jose.JOSEException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class Oauth2ServiceImplTest {
  private Oauth2Service oauth2Service;
  private AppClientRepository appClientRepository;
  private AppClientScopeRepository appClientScopeRepository;
  private AppAccessTokenRepository appAccessTokenRepository;
  private AppRefreshTokenRepository appRefreshTokenRepository;
  private AppClient appClient;
  private AppClientScope appClientScope;
  private AppRefreshToken appRefreshToken;

  @BeforeEach
  public void setUp() throws JOSEException {

    appClientRepository = Mockito.mock(AppClientRepository.class);
    appClientScopeRepository = Mockito.mock(AppClientScopeRepository.class);
    appAccessTokenRepository = Mockito.mock(AppAccessTokenRepository.class);
    appRefreshTokenRepository = Mockito.mock(AppRefreshTokenRepository.class);
    oauth2Service =
        new Oauth2ServiceImpl(
            Oauth2Fixture.SECRET_KEY,
            appClientRepository,
            appClientScopeRepository,
            appAccessTokenRepository,
            appRefreshTokenRepository);
    MockitoAnnotations.openMocks(this);
    appClient = AppClientFixture.buildAppClient(1L);
    appClientScope = AppClientScopeFixture.buildAppClientScope(1L);
    appRefreshToken = AppRefreshTokenFixture.buildAppRefreshToken(1L);

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

    Mockito.when(
            this.appClientRepository.findByClientIdAndEntityStatus(
                Mockito.eq(appClient.getClientId()), Mockito.eq(Status.ACTIVE)))
        .thenReturn(Optional.of(appClient));
    Mockito.when(
            this.appClientScopeRepository.findByAppClientIdAndEntityStatus(
                Mockito.eq(1L), Mockito.eq(Status.ACTIVE)))
        .thenReturn(Optional.of(appClientScope));

    Mockito.when(this.appRefreshTokenRepository.save(Mockito.any())).thenReturn(appRefreshToken);
  }

  @Test
  public void whenClientCredentialsLogin_ShouldReturnLoginResponse()
      throws JOSEException, UnsupportedEncodingException {

    var loginResponse = this.oauth2Service.loginClient("TEST01", "TEST-CLIENT-SECRET");
    Assertions.assertEquals(loginResponse.getClientName(), appClient.getClientName());
    Assertions.assertEquals(loginResponse.getScopes(), appClientScope.getScopes());
    Assertions.assertEquals(loginResponse.getAccessToken(), AppAccessTokenFixture.TOKEN);
    Assertions.assertEquals(loginResponse.getRefreshToken(), AppRefreshTokenFixture.TOKEN);
    Assertions.assertEquals(loginResponse.getExpiresIn(), appClient.getAccessTokenExpirationTime());

    Mockito.verify(appClientRepository, Mockito.times(2))
        .findByClientIdAndEntityStatus(
            Mockito.eq(appClient.getClientId()), Mockito.eq(Status.ACTIVE));
    Mockito.verify(appClientScopeRepository, Mockito.times(1))
        .findByAppClientIdAndEntityStatus(Mockito.eq(1L), Mockito.eq(Status.ACTIVE));
    Mockito.verify(appRefreshTokenRepository, Mockito.times(1)).save(Mockito.any());
  }

  @Test
  public void whenWrongClientCredentialsLogin_ShouldReturnWrongCredentialsException()
      throws UnsupportedEncodingException, JOSEException {
    Exception exception =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              var loginResponse = this.oauth2Service.loginClient("TEST02", "TEST-CLIENT-SECRET");
            });
    var expectedWrongCredentialExceptionMessage = "Wrong Credentials";

    Assertions.assertEquals(exception.getMessage(), expectedWrongCredentialExceptionMessage);
  }
}
