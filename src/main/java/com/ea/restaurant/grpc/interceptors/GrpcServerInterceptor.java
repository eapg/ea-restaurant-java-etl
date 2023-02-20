package com.ea.restaurant.grpc.interceptors;

import com.ea.restaurant.constants.RequestMetadataConstants;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class GrpcServerInterceptor implements ServerInterceptor {
  public static final Context.Key<Object> CONTEXT_METADATA =
      Context.key(RequestMetadataConstants.CONTEXT_KEY_NAME_METADATA);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {

    var updatedContext = Context.current().withValue(CONTEXT_METADATA, metadata);

    return Contexts.interceptCall(updatedContext, serverCall, metadata, serverCallHandler);
  }
}
