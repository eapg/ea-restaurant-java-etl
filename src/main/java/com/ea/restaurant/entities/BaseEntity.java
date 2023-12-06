package com.ea.restaurant.entities;

import com.ea.restaurant.constants.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @NotNull private Long updatedBy;
  @NotNull private Long createdBy;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Status entityStatus;

  private Date createdDate;
  private Date updatedDate;

  public BaseEntity(Long id) {
    this.id = id;
  }
}
