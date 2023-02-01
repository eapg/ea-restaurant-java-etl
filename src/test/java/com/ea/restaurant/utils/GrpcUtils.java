package com.ea.restaurant.utils;

import io.grpc.stub.StreamObserver;
import org.mockito.Mockito;

public class GrpcUtils {

  @SuppressWarnings("unchecked")
  public static <T> StreamObserver<T> getMockedStreamObserver() {
    return Mockito.mock(StreamObserver.class);
  }
}
