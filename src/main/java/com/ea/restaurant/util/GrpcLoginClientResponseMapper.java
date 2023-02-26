package com.ea.restaurant.util;

import com.ea.restaurant.dtos.LoginResponseDto;
import com.ea.restaurant.grpc.LoginClientResponse;

public final class GrpcLoginClientResponseMapper {

  public static LoginClientResponse mapLoginResponseToGrpcLoginResponse(
      LoginResponseDto loginResponseDto) {
    return LoginClientResponse.newBuilder()
        .setAccessToken(loginResponseDto.getAccessToken())
        .setRefreshToken(loginResponseDto.getRefreshToken())
        .setExpiresIn(loginResponseDto.getExpiresIn())
        .setScopes(loginResponseDto.getScopes())
        .setClientName(loginResponseDto.getClientName())
        .build();
  }
}
