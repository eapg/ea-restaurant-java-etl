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
@Table(name = "app_clients_scopes")
public class AppClientScope extends BaseEntity {
  @NotNull private Long appClientId;
  @NotNull private String scopes;

  public AppClientScope(Long id) {
    super(id);
  }
}
