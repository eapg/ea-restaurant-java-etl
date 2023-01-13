package com.ea.restaurant.entities;

import com.ea.restaurant.constants.Status;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Setter
public abstract class BaseEntity {

  private long id;
  private long updatedBy;
  private long createdBy;
  private Status entityStatus;
  private Instant createdDate;
  private Instant updatedDate;

  public BaseEntity(long id) {

    this.id = id;
  }
}
