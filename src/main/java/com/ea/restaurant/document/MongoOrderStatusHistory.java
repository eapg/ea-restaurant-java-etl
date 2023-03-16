package com.ea.restaurant.document;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "order_status_histories")
public class MongoOrderStatusHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private ObjectId id;

  @Field("order_id")
  @NotNull
  private Long orderId;

  @Field("from_time")
  private Instant fromTime;

  @Field("to_time")
  private Instant toTime;

  @Field("from_status")
  @Enumerated(EnumType.STRING)
  private OrderStatus fromStatus;

  @Field("to_status")
  @Enumerated(EnumType.STRING)
  private OrderStatus toStatus;

  @Field("etl_status")
  @Enumerated
  private EtlStatus etlStatus;

  @Field("update_by")
  @NotNull
  private Long updatedBy;

  @Field("created_by")
  @NotNull
  private Long createdBy;

  @Field("entity_status")
  @NotNull
  @Enumerated(EnumType.STRING)
  private Status entityStatus;

  @Field("created_date")
  private Instant createdDate;

  @Field("updated_date")
  private Instant updatedDate;

  public MongoOrderStatusHistory(ObjectId id) {
    this.id = id;
  }
}
