package com.ea.restaurant.grpc.interceptors;

import com.ea.restaurant.constants.RequestMetadataConstants;
import com.ea.restaurant.util.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import java.text.ParseException;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@GrpcGlobalServerInterceptor
public class GrpcServerInterceptor implements ServerInterceptor {
  public static final Context.Key<Object> CONTEXT_METADATA =
      Context.key(RequestMetadataConstants.CONTEXT_KEY_NAME_METADATA);

  @Autowired
  @Value("${oauth2.secret.key}")
  private String secretKey;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {

    try {
      Oauth2Util.validateEndpointProtection(
          metadata, serverCall.getMethodDescriptor().getBareMethodName(), secretKey);
    } catch (BadJOSEException | ParseException | JOSEException e) {
      throw new RuntimeException(e);
    }

    var updatedContext = Context.current().withValue(CONTEXT_METADATA, metadata);

    return Contexts.interceptCall(updatedContext, serverCall, metadata, serverCallHandler);
  }
}
