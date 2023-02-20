package com.ea.restaurant.grpc;

import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.util.GrpcLoginClientResponseMapper;
import com.ea.restaurant.util.GrpcUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcOauth2ServiceImpl extends Oauth2ServiceGrpc.Oauth2ServiceImplBase {
  private final Oauth2Service oauth2Service;

  public GrpcOauth2ServiceImpl(Oauth2Service oauth2Service) {
    this.oauth2Service = oauth2Service;
  }

  @Override
  public void loginClient(
      LoginClientRequest request, StreamObserver<LoginClientResponse> responseObserver) {

    var AuthMetadata = GrpcUtil.getAuthMetadataFromInterceptor();

    var credentials = GrpcUtil.getCredentialsFromBasicAuthToken(AuthMetadata);

    try {
      var loginClient =
          this.oauth2Service.loginClient(credentials.clientId(), credentials.clientSecret());
      responseObserver.onNext(
          GrpcLoginClientResponseMapper.mapLoginResponseToGrpcLoginResponse(loginClient));
    } catch (Exception e) {
      responseObserver.onError(e);
    }
    responseObserver.onCompleted();
  }
}
