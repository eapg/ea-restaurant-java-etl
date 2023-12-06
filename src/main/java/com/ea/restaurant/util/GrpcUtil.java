package com.ea.restaurant.util;

import com.ea.restaurant.constants.RequestMetadataConstants;
import com.ea.restaurant.exceptions.UnauthorizedException;
import com.ea.restaurant.grpc.interceptors.GrpcServerInterceptor;
import com.ea.restaurant.record.Oauth2ClientCredentials;
import io.grpc.Metadata;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

public class GrpcUtil {

  public static Oauth2ClientCredentials getCredentialsFromBasicAuthToken(Metadata metadata) {
    var authorization =
        metadata.get(
            Metadata.Key.of(
                RequestMetadataConstants.KEY_NAME_FOR_AUTHORIZATION_METADATA,
                Metadata.ASCII_STRING_MARSHALLER));

    var basicToken = extractBasicToken(authorization);
    return getBasicAuthenticationDecoded(basicToken);
  }

  public static String extractBasicToken(String authorization) {
    return Optional.ofNullable(authorization)
        .map(auth -> authorization.substring("Basic".length()))
        .map(String::trim)
        .orElseThrow(UnauthorizedException::new);
  }

  public static Metadata getAuthMetadataFromInterceptor() {
    return ((Metadata) GrpcServerInterceptor.CONTEXT_METADATA.get());
  }

  public static Oauth2ClientCredentials getBasicAuthenticationDecoded(String basicToken) {
    var credentialsDecoded = Base64.getDecoder().decode(basicToken);
    var credentials = new String(credentialsDecoded, StandardCharsets.UTF_8);
    var credentialParts = Stream.of(credentials.split(":")).toList();
    return new Oauth2ClientCredentials(credentialParts.get(0), credentialParts.get(1));
  }
}
