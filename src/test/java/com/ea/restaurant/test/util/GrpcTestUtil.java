package com.ea.restaurant.test.util;

import io.grpc.stub.StreamObserver;
import org.mockito.Mockito;

public class GrpcTestUtil {

  @SuppressWarnings("unchecked")
  public static <T> StreamObserver<T> getMockedStreamObserver() {
    return Mockito.mock(StreamObserver.class);
  }
}
