package com.ea.restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "app_clients")
public class AppClient extends BaseEntity {
  @NotNull private String clientName;
  @NotNull private String clientId;
  @NotNull private String clientSecret;
  @NotNull private Integer accessTokenExpirationTime;
  @NotNull private Integer refreshTokenExpirationTime;

  public AppClient(Long id) {
    super(id);
  }
}
