package com.ea.restaurant.entities;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "order_status_histories")
public class OrderStatusHistory extends BaseEntity {
  @NotNull private Long orderId;
  private Date fromTime;
  private Date toTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus fromStatus;

  @Enumerated(EnumType.STRING)
  private OrderStatus toStatus;

  private String mongoOrderStatusHistoryUuid;

  @Enumerated(EnumType.STRING)
  private EtlStatus etlStatus;

  public OrderStatusHistory(Long id) {
    super(id);
  }
}
