package com.ea.restaurant.test.util;

import com.ea.restaurant.record.Oauth2ClientCredentials;
import com.ea.restaurant.util.GrpcUtil;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class GrpcTestUtil {

  @SuppressWarnings("unchecked")
  public static <T> StreamObserver<T> getMockedStreamObserver() {
    return Mockito.mock(StreamObserver.class);
  }

  public static MockedStatic<GrpcUtil> mockGrpcUtil(
      Metadata metadata, Oauth2ClientCredentials clientCredentials) {
    var mockedGrpcUtil = Mockito.mockStatic(GrpcUtil.class);

    Mockito.when(GrpcUtil.getAuthMetadataFromInterceptor()).thenReturn(metadata);

    Mockito.when(GrpcUtil.getCredentialsFromBasicAuthToken(Mockito.eq(metadata)))
        .thenReturn(clientCredentials);

    return mockedGrpcUtil;
  }
}
