package com.ea.restaurant.util;

import com.ea.restaurant.dtos.Oauth2TokenResponseDto;
import com.ea.restaurant.grpc.Oauth2TokenResponse;

public final class GrpcOauth2TokenResponseMapper {

  public static Oauth2TokenResponse mapLoginResponseToGrpcLoginResponse(
      Oauth2TokenResponseDto oauth2TokenResponseDto) {
    return Oauth2TokenResponse.newBuilder()
        .setAccessToken(oauth2TokenResponseDto.getAccessToken())
        .setRefreshToken(oauth2TokenResponseDto.getRefreshToken())
        .setExpiresIn(oauth2TokenResponseDto.getExpiresIn())
        .setScopes(oauth2TokenResponseDto.getScopes())
        .setClientName(oauth2TokenResponseDto.getClientName())
        .build();
  }
}
