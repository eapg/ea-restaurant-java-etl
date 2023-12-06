package com.ea.restaurant.entities;

import com.ea.restaurant.constants.Oauth2;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "app_refresh_tokens")
public class AppRefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @NotNull private String token;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Oauth2.GranType grantType;

  private Long appClientId;

  public AppRefreshToken(Long id) {
    this.id = id;
  }
}
