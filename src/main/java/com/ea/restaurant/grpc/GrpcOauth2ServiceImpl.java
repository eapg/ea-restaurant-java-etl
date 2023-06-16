package com.ea.restaurant.grpc;

import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.util.GrpcOauth2TokenResponseMapper;
import com.ea.restaurant.util.GrpcUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@GrpcService
public class GrpcOauth2ServiceImpl extends Oauth2ServiceGrpc.Oauth2ServiceImplBase {
  private final Oauth2Service oauth2Service;

  public GrpcOauth2ServiceImpl(Oauth2Service oauth2Service) {
    this.oauth2Service = oauth2Service;
  }

  @Override
  public void loginClient(
      NotParametersRequest request, StreamObserver<Oauth2TokenResponse> responseObserver) throws UnsupportedEncodingException, JOSEException {

      var authMetadata = GrpcUtil.getAuthMetadataFromInterceptor();

      var credentials = GrpcUtil.getCredentialsFromBasicAuthToken(authMetadata);

      var loginClient =
          this.oauth2Service.loginClient(credentials.clientId(), credentials.clientSecret());
      responseObserver.onNext(
          GrpcOauth2TokenResponseMapper.mapLoginResponseToGrpcLoginResponse(loginClient));

    responseObserver.onCompleted();
  }

  @Override
  public void refreshToken(
      RefreshTokenRequest request, StreamObserver<Oauth2TokenResponse> responseObserver) throws BadJOSEException, ParseException, JOSEException {

      var refreshTokenResponse =
          this.oauth2Service.refreshToken(
              request.getRefreshToken(),
              request.getAccessToken(),
              request.getClientId(),
              request.getClientSecret());
      responseObserver.onNext(
          GrpcOauth2TokenResponseMapper.mapLoginResponseToGrpcLoginResponse(refreshTokenResponse));

    responseObserver.onCompleted();
  }
}
