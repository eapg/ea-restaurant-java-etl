package com.ea.restaurant.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Document(collection = "order_status_histories")
@Table(name = "order_status_histories")
public class OrderStatusHistory extends BaseEntity {
  @NotNull private Long orderId;
  private Instant fromTime;
  private Instant toTime;
  private String fromStatus;
  private String toStatus;
  private String mongodbOrderStatusHistoryUuid;
  private String etlStatus;

  public OrderStatusHistory(Long id) {
    super(id);
  }
}
