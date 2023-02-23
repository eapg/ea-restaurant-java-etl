package com.ea.restaurant.grpc;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.ea.restaurant.entities.AppRefreshToken;
import com.ea.restaurant.record.Oauth2ClientCredentials;
import com.ea.restaurant.repository.AppAccessTokenRepository;
import com.ea.restaurant.repository.AppClientRepository;
import com.ea.restaurant.repository.AppClientScopeRepository;
import com.ea.restaurant.repository.AppRefreshTokenRepository;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.service.impl.Oauth2ServiceImpl;
import com.ea.restaurant.test.fixture.AppClientFixture;
import com.ea.restaurant.test.fixture.AppClientScopeFixture;
import com.ea.restaurant.test.fixture.AppRefreshTokenFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.test.util.GrpcTestUtil;
import com.ea.restaurant.test.util.Oauth2TestUtil;
import com.ea.restaurant.util.GrpcUtil;
import com.nimbusds.jose.JOSEException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GrpcOauth2ServiceImplIntegrationTest {

  private AppClientRepository appClientRepository;
  private AppClientScopeRepository appClientScopeRepository;
  private AppRefreshTokenRepository appRefreshTokenRepository;
  private AppAccessTokenRepository appAccessTokenRepository;
  private Oauth2Service oauth2Service;
  private GrpcOauth2ServiceImpl grpcOauth2Service;
  private AutoCloseable mockedAnnotations;
  private AppClient appClient;
  private AppClientScope appClientScope;
  private AppRefreshToken appRefreshToken;

  @BeforeEach
  public void setUp() {
    appClientRepository = Mockito.mock(AppClientRepository.class);
    appClientScopeRepository = Mockito.mock(AppClientScopeRepository.class);
    appAccessTokenRepository = Mockito.mock(AppAccessTokenRepository.class);
    appRefreshTokenRepository = Mockito.mock(AppRefreshTokenRepository.class);
    oauth2Service =
        Mockito.spy(
            new Oauth2ServiceImpl(
                Oauth2Fixture.SECRET_KEY,
                appClientRepository,
                appClientScopeRepository,
                appAccessTokenRepository,
                appRefreshTokenRepository));

    grpcOauth2Service = new GrpcOauth2ServiceImpl(oauth2Service);

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
  public void whenLoginClientCredentials_ShouldReturnExpectedLoginResponse()
      throws JOSEException, UnsupportedEncodingException {

    var testMetadata =
        Oauth2Fixture.buildMetadataWithBasicToken(
            Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);
    var testClientCredentials =
        new Oauth2ClientCredentials(Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);

    try (var mockedGrpcUtil = GrpcTestUtil.mockGrpcUtil(testMetadata, testClientCredentials);
        var mockedOauth2Util = Oauth2TestUtil.mockOauth2Util(appClient, appClientScope)) {
      var mockedStreamObserver = GrpcTestUtil.<LoginClientResponse>getMockedStreamObserver();
      this.grpcOauth2Service.loginClient(null, mockedStreamObserver);

      Mockito.verify(appClientRepository, Mockito.times(2))
          .findByClientIdAndEntityStatus(
              Mockito.eq(appClient.getClientId()), Mockito.eq(Status.ACTIVE));
      Mockito.verify(appClientScopeRepository, Mockito.times(1))
          .findByAppClientIdAndEntityStatus(Mockito.eq(1L), Mockito.eq(Status.ACTIVE));
      Mockito.verify(appRefreshTokenRepository, Mockito.times(1)).save(Mockito.any());
      Mockito.verify(oauth2Service, Mockito.times(1))
          .loginClient(
              Mockito.eq(testClientCredentials.clientId()),
              Mockito.eq(testClientCredentials.clientSecret()));

      Mockito.verify(mockedStreamObserver, Mockito.times(1)).onNext(Mockito.any());
      Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
      mockedGrpcUtil.verify(GrpcUtil::getAuthMetadataFromInterceptor);
      mockedGrpcUtil.verify(
          () -> GrpcUtil.getCredentialsFromBasicAuthToken(Mockito.eq(testMetadata)));
    }
  }
}
