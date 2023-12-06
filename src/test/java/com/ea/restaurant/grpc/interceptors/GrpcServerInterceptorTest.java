package com.ea.restaurant.grpc.interceptors;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.exceptions.UnauthorizedException;
import com.ea.restaurant.test.fixture.AppAccessTokenFixture;
import com.ea.restaurant.test.fixture.AppClientFixture;
import com.ea.restaurant.test.fixture.AppClientScopeFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import java.text.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GrpcServerInterceptorTest {

  private String secretKey;
  private GrpcServerInterceptor interceptor;
  private ServerCall mockedServerCall;
  private ServerCallHandler mockedServerCallHandle;
  private MethodDescriptor mockedDescriptor;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  void setUp() {
    secretKey = "thisIsTheSecretKeyForTest-Etl001";
    interceptor = new GrpcServerInterceptor();
    mockedServerCall = Mockito.mock(ServerCall.class);
    mockedDescriptor = Mockito.mock(io.grpc.MethodDescriptor.class);
    mockedServerCallHandle = Mockito.mock(ServerCallHandler.class);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @Test
  void whenInterceptCall_shouldValidateEndpointProtection() throws JOSEException {
    var endpointName = "insertMongoOrderStatusHistoriesFromPythonEtl";
    var appClient = AppClientFixture.buildAppClient(1L);
    var appClientScopes = AppClientScopeFixture.buildAppClientScope(1L);
    var token =
        Oauth2Util.buildClientCredentialsToken(
            appClient, appClientScopes, secretKey, Oauth2.TokenType.ACCESS_TOKEN);
    var metadata = Oauth2Fixture.buildMetaDataWithBearerToken(token);

    Mockito.when(mockedServerCall.getMethodDescriptor()).thenReturn(mockedDescriptor);

    Mockito.when(mockedDescriptor.getBareMethodName()).thenReturn(endpointName);
    var mockedOauthUtil = Mockito.mockStatic(Oauth2Util.class);
    interceptor.interceptCall(mockedServerCall, metadata, mockedServerCallHandle);

    mockedOauthUtil.verify(
        () ->
            Oauth2Util.validateEndpointProtection(
                Mockito.eq(metadata), Mockito.eq(endpointName), Mockito.any()));
  }

  @Test
  public void whenInterceptCallWithInvalidToken_shouldThrowException()
      throws BadJOSEException, ParseException, JOSEException {
    var endpointName = "insertMongoOrderStatusHistoriesFromPythonEtl";
    var token = AppAccessTokenFixture.TOKEN;
    var metadata = Oauth2Fixture.buildMetaDataWithBearerToken(token);
    Mockito.when(mockedServerCall.getMethodDescriptor()).thenReturn(mockedDescriptor);

    Mockito.when(mockedDescriptor.getBareMethodName()).thenReturn(endpointName);
    var mockedOauthUtil = Mockito.mockStatic(Oauth2Util.class);
    mockedOauthUtil
        .when(
            () ->
                Oauth2Util.validateEndpointProtection(
                    Mockito.eq(metadata), Mockito.eq(endpointName), Mockito.any()))
        .thenThrow(UnauthorizedException.class);

    Assertions.assertThrows(
        UnauthorizedException.class,
        () -> interceptor.interceptCall(mockedServerCall, metadata, mockedServerCallHandle));
  }
}
