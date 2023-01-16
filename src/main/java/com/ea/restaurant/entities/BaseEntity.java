package com.ea.restaurant.entities;

import com.ea.restaurant.constants.Status;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull private Long updatedBy;
  @NotNull private Long createdBy;
  @NotNull private Status entityStatus;
  private Instant createdDate;
  private Instant updatedDate;

  public BaseEntity(Long id) {

    this.id = id;
  }
}
