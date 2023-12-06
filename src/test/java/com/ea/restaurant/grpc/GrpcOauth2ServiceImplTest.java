package com.ea.restaurant.grpc;

import com.ea.restaurant.dtos.Oauth2TokenResponseDto;
import com.ea.restaurant.exceptions.WrongCredentialsException;
import com.ea.restaurant.record.Oauth2ClientCredentials;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.test.fixture.AppAccessTokenFixture;
import com.ea.restaurant.test.fixture.AppClientFixture;
import com.ea.restaurant.test.fixture.AppRefreshTokenFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.test.util.GrpcTestUtil;
import com.ea.restaurant.util.GrpcOauth2TokenResponseMapper;
import com.ea.restaurant.util.GrpcUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GrpcOauth2ServiceImplTest {
  private Oauth2Service oauth2Service;
  private GrpcOauth2ServiceImpl grpcOauth2Service;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  public void setUp() {
    oauth2Service = Mockito.mock(Oauth2Service.class);
    grpcOauth2Service = new GrpcOauth2ServiceImpl(oauth2Service);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  public void whenLoginClientCredentials_ShouldReturnExpectedLoginResponse()
      throws UnsupportedEncodingException, JOSEException {
    var testMetadata =
        Oauth2Fixture.buildMetadataWithBasicToken(
            Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);
    var testClientCredentials =
        new Oauth2ClientCredentials(Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);

    try (var mockedGrpcUtil = GrpcTestUtil.mockGrpcUtil(testMetadata, testClientCredentials)) {
      var mockedStreamObserver = GrpcTestUtil.<Oauth2TokenResponse>getMockedStreamObserver();
      var loginResponse =
          Oauth2TokenResponseDto.builder()
              .accessToken(AppAccessTokenFixture.TOKEN)
              .refreshToken(AppRefreshTokenFixture.TOKEN)
              .clientName(AppClientFixture.TESTING_CLIENT_NAME)
              .scopes(Oauth2Fixture.TEST_SCOPES)
              .expiresIn(5)
              .build();
      Mockito.when(
              this.oauth2Service.loginClient(
                  Mockito.eq(testClientCredentials.clientId()),
                  Mockito.eq(testClientCredentials.clientSecret())))
          .thenReturn(loginResponse);
      this.grpcOauth2Service.loginClient(null, mockedStreamObserver);
      var expectedGrpcLoginResponse =
          GrpcOauth2TokenResponseMapper.mapLoginResponseToGrpcLoginResponse(loginResponse);

      Mockito.verify(mockedStreamObserver, Mockito.times(1))
          .onNext(Mockito.eq(expectedGrpcLoginResponse));
      Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
      mockedGrpcUtil.verify(GrpcUtil::getAuthMetadataFromInterceptor);
      mockedGrpcUtil.verify(
          () -> GrpcUtil.getCredentialsFromBasicAuthToken(Mockito.eq(testMetadata)));
    }
  }

  @Test
  public void whenWrongClientCredentials_ShouldThrowUnauthorizedException()
      throws UnsupportedEncodingException, JOSEException {
    var testMetadata =
        Oauth2Fixture.buildMetadataWithBasicToken(
            Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);

    var testClientWrongCredentials =
        new Oauth2ClientCredentials(
            Oauth2Fixture.TEST_WRONG_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);

    try (var mockedGrpcUtil = GrpcTestUtil.mockGrpcUtil(testMetadata, testClientWrongCredentials)) {

      var mockedStreamObserver = GrpcTestUtil.<Oauth2TokenResponse>getMockedStreamObserver();
      Mockito.when(
              this.oauth2Service.loginClient(
                  Mockito.eq(Oauth2Fixture.TEST_WRONG_CLIENT_ID),
                  Mockito.eq(Oauth2Fixture.TEST_CLIENT_SECRET)))
          .thenThrow(new WrongCredentialsException());
      this.grpcOauth2Service.loginClient(null, mockedStreamObserver);

      Mockito.verify(mockedStreamObserver, Mockito.times(1)).onError(Mockito.any());
    }
  }

  @Test
  public void whenExpiredAccessToken_ShouldReturnNewAccessToken()
      throws BadJOSEException, ParseException, JOSEException {
    var refreshTokenRequest =
        RefreshTokenRequest.newBuilder()
            .setAccessToken(AppAccessTokenFixture.EXPIRE_TOKEN)
            .setRefreshToken(AppRefreshTokenFixture.TOKEN)
            .setClientId(Oauth2Fixture.TEST_CLIENT_ID)
            .setClientSecret(Oauth2Fixture.TEST_CLIENT_SECRET)
            .build();
    var refreshTokenResponse =
        Oauth2TokenResponseDto.builder()
            .accessToken(AppAccessTokenFixture.TOKEN)
            .refreshToken(AppRefreshTokenFixture.TOKEN)
            .clientName(AppClientFixture.TESTING_CLIENT_NAME)
            .scopes(Oauth2Fixture.TEST_SCOPES)
            .expiresIn(5)
            .build();
    Mockito.when(
            this.oauth2Service.refreshToken(
                Mockito.eq(AppRefreshTokenFixture.TOKEN),
                Mockito.eq(AppAccessTokenFixture.EXPIRE_TOKEN),
                Mockito.eq(Oauth2Fixture.TEST_CLIENT_ID),
                Mockito.eq(Oauth2Fixture.TEST_CLIENT_SECRET)))
        .thenReturn(refreshTokenResponse);
    var mockedStreamObserver = GrpcTestUtil.<Oauth2TokenResponse>getMockedStreamObserver();
    this.grpcOauth2Service.refreshToken(refreshTokenRequest, mockedStreamObserver);
    var expectedGrpcRefreshTokenResponse =
        GrpcOauth2TokenResponseMapper.mapLoginResponseToGrpcLoginResponse(refreshTokenResponse);
    Mockito.verify(this.oauth2Service, Mockito.times(1))
        .refreshToken(
            Mockito.eq(AppRefreshTokenFixture.TOKEN),
            Mockito.eq(AppAccessTokenFixture.EXPIRE_TOKEN),
            Mockito.eq(Oauth2Fixture.TEST_CLIENT_ID),
            Mockito.eq(Oauth2Fixture.TEST_CLIENT_SECRET));

    Mockito.verify(mockedStreamObserver, Mockito.times(1))
        .onNext(Mockito.eq(expectedGrpcRefreshTokenResponse));
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
  }
}
