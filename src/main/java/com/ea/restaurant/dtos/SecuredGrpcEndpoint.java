package com.ea.restaurant.dtos;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Builder
public class SecuredGrpcEndpoint {
  private String grpcEndpointName;
  private List<String> scopes;
}
