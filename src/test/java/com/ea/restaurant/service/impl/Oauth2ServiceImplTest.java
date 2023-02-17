package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.entities.AppRefreshToken;
import com.ea.restaurant.repository.AppAccessTokenRepository;
import com.ea.restaurant.repository.AppClientRepository;
import com.ea.restaurant.repository.AppClientScopeRepository;
import com.ea.restaurant.repository.AppRefreshTokenRepository;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.test.fixture.AppAccessTokenFixture;
import com.ea.restaurant.test.fixture.AppClientFixture;
import com.ea.restaurant.test.fixture.AppClientScopeFixture;
import com.ea.restaurant.test.fixture.AppRefreshTokenFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.test.util.Oauth2TestUtil;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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
  private AutoCloseable mockedAnnotations;

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
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
    appClient = AppClientFixture.buildAppClient(1L);
    appClientScope = AppClientScopeFixture.buildAppClientScope(1L);
    appRefreshToken = AppRefreshTokenFixture.buildAppRefreshToken(1L);

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

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  public void whenClientCredentialsLogin_ShouldReturnLoginResponse()
      throws JOSEException, UnsupportedEncodingException {

    try (var ignored = Oauth2TestUtil.mockOauth2Util(appClient, appClientScope)) {

      var loginResponse =
          this.oauth2Service.loginClient(
              Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);
      Assertions.assertEquals(loginResponse.getClientName(), appClient.getClientName());
      Assertions.assertEquals(loginResponse.getScopes(), appClientScope.getScopes());
      Assertions.assertEquals(loginResponse.getAccessToken(), AppAccessTokenFixture.TOKEN);
      Assertions.assertEquals(loginResponse.getRefreshToken(), AppRefreshTokenFixture.TOKEN);
      Assertions.assertEquals(
          loginResponse.getExpiresIn(), appClient.getAccessTokenExpirationTime());

      Mockito.verify(appClientRepository, Mockito.times(2))
          .findByClientIdAndEntityStatus(
              Mockito.eq(appClient.getClientId()), Mockito.eq(Status.ACTIVE));
      Mockito.verify(appClientScopeRepository, Mockito.times(1))
          .findByAppClientIdAndEntityStatus(Mockito.eq(1L), Mockito.eq(Status.ACTIVE));
      Mockito.verify(appRefreshTokenRepository, Mockito.times(1)).save(Mockito.any());
    }
  }

  @Test
  public void whenWrongClientCredentialsLogin_ShouldReturnWrongCredentialsException() {
    Exception exception =
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
                this.oauth2Service.loginClient(
                    Oauth2Fixture.TEST_WRONG_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET));
    var expectedWrongCredentialExceptionMessage = "Wrong Credentials";

    Assertions.assertEquals(exception.getMessage(), expectedWrongCredentialExceptionMessage);
  }

  @Test
  public void whenRefreshExpiredAccessToken_ShouldReturnNewAccessToken()
      throws JOSEException, BadJOSEException, ParseException {

    try (var mockedOauth2Util = Oauth2TestUtil.mockOauth2Util(appClient, appClientScope)) {
      mockedOauth2Util
          .when(
              () ->
                  Oauth2Util.validateToken(
                      Mockito.eq(AppAccessTokenFixture.EXPIRE_TOKEN),
                      Mockito.eq(Oauth2Fixture.SECRET_KEY)))
          .thenThrow(BadJWTException.class);

      Mockito.when(
              this.appRefreshTokenRepository.findByAccessTokenAndRefreshTokenAndClientId(
                  Mockito.eq(AppAccessTokenFixture.EXPIRE_TOKEN),
                  Mockito.eq(AppRefreshTokenFixture.TOKEN),
                  Mockito.eq(this.appClient.getClientId())))
          .thenReturn(Optional.of(appRefreshToken));
      Mockito.when(
              this.appClientRepository.findByIdAndEntityStatus(
                  Mockito.eq(appRefreshToken.getAppClientId()), Mockito.eq(Status.ACTIVE)))
          .thenReturn(Optional.of(appClient));
      Mockito.when(
              this.appClientScopeRepository.findByAppClientIdAndEntityStatus(
                  Mockito.eq(appClient.getId()), Mockito.eq(Status.ACTIVE)))
          .thenReturn(Optional.of(appClientScope));
      var refreshTokenResponse =
          this.oauth2Service.refreshToken(
              AppRefreshTokenFixture.TOKEN,
              AppAccessTokenFixture.EXPIRE_TOKEN,
              Oauth2Fixture.TEST_CLIENT_ID,
              Oauth2Fixture.TEST_CLIENT_SECRET);

      Assertions.assertNotEquals(
          AppAccessTokenFixture.EXPIRE_TOKEN, refreshTokenResponse.getAccessToken());
      Mockito.verify(appRefreshTokenRepository, Mockito.times(1))
          .findByAccessTokenAndRefreshTokenAndClientId(
              Mockito.eq(AppAccessTokenFixture.EXPIRE_TOKEN),
              Mockito.eq(AppRefreshTokenFixture.TOKEN),
              Mockito.eq(this.appClient.getClientId()));
      Mockito.verify(appClientRepository, Mockito.times(1))
          .findByIdAndEntityStatus(
              Mockito.eq(appRefreshToken.getAppClientId()), Mockito.eq(Status.ACTIVE));
      Mockito.verify(appClientScopeRepository, Mockito.times(1))
          .findByAppClientIdAndEntityStatus(
              Mockito.eq(appClient.getId()), Mockito.eq(Status.ACTIVE));
    }
  }

  @Test
  public void whenRefreshUnExpiredAccessToken_ShouldReturnSameAccessToken()
      throws BadJOSEException, ParseException, JOSEException {

    var accessToken = AppAccessTokenFixture.buildAccessToken(appClient, appClientScope);
    var refreshToken = AppRefreshTokenFixture.buildRefreshToken(appClient, appClientScope);
    var tokenDecoded = AppAccessTokenFixture.buildTokenDecoded(accessToken);

    try (var mockedOauth2Util = Oauth2TestUtil.mockOauth2Util(appClient, appClientScope)) {

      mockedOauth2Util
          .when(
              () ->
                  Oauth2Util.getTokenDecoded(
                      Mockito.eq(accessToken), Mockito.eq(Oauth2Fixture.SECRET_KEY)))
          .thenReturn(tokenDecoded);

      var refreshTokenResponse =
          this.oauth2Service.refreshToken(
              refreshToken,
              accessToken,
              Oauth2Fixture.TEST_CLIENT_ID,
              Oauth2Fixture.TEST_CLIENT_SECRET);

      Assertions.assertEquals(accessToken, refreshTokenResponse.getAccessToken());
      mockedOauth2Util.verify(
          () ->
              Oauth2Util.getTokenDecoded(
                  Mockito.eq(accessToken), Mockito.eq(Oauth2Fixture.SECRET_KEY)));
    }
  }
}
