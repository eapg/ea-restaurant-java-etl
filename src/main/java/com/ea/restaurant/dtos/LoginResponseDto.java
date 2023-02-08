package com.ea.restaurant.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Builder
public class LoginResponseDto {
  private String accessToken;
  private String refreshToken;
  private Integer expiresIn;
  private String scopes;
  private String clientName;
}
