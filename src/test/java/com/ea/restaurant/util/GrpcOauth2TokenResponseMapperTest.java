package com.ea.restaurant.util;

import com.ea.restaurant.dtos.Oauth2TokenResponseDto;
import com.ea.restaurant.test.fixture.AppAccessTokenFixture;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GrpcOauth2TokenResponseMapperTest {

  @Test
  public void whenLoginResponseProvide_shouldMapToGrpcLoginResponse() {
    var loginResponse =
        Oauth2TokenResponseDto.builder()
            .clientName(Oauth2Fixture.TEST_CLIENT_NAME)
            .accessToken(AppAccessTokenFixture.TOKEN)
            .refreshToken(AppAccessTokenFixture.TOKEN)
            .scopes(Oauth2Fixture.TEST_SCOPES)
            .expiresIn(Oauth2Fixture.TEST_EXPIRATION_TIME)
            .build();
    var mappedLoginResponse =
        GrpcOauth2TokenResponseMapper.mapLoginResponseToGrpcLoginResponse(loginResponse);
    Assertions.assertEquals(loginResponse.getClientName(), mappedLoginResponse.getClientName());
    Assertions.assertEquals(loginResponse.getAccessToken(), mappedLoginResponse.getAccessToken());
    Assertions.assertEquals(loginResponse.getRefreshToken(), mappedLoginResponse.getRefreshToken());
    Assertions.assertEquals(loginResponse.getScopes(), mappedLoginResponse.getScopes());
    Assertions.assertEquals(loginResponse.getExpiresIn(), mappedLoginResponse.getExpiresIn());
  }
}
