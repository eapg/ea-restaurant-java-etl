package com.ea.restaurant.test.util;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class AuthenticationCallCredentials extends CallCredentials {
  private Metadata metadata;

  public AuthenticationCallCredentials(Metadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public void applyRequestMetadata(
      RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
    executor.execute(
        () -> {
          try {

            metadataApplier.apply(this.metadata);
          } catch (Throwable e) {
            metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
          }
        });
  }

  @Override
  public void thisUsesUnstableApi() {}
}
