package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.grpc.MongoOrderStatusHistory;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import java.time.Instant;

public class GrpcMongoOrderStatusHistoryFixture {
  public static final Long ORDER_ID = 1L;
  public static final Instant FROM_TIME = Instant.EPOCH;
  public static final Instant TO_TIME = Instant.EPOCH;
  public static final OrderStatus FROM_STATUS = OrderStatus.NEW_ORDER;
  public static final OrderStatus TO_STATUS = OrderStatus.IN_PROCESS;
  public static final EtlStatus ETL_STATUS = EtlStatus.UNPROCESSED;
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(String id) {

    return MongoOrderStatusHistory.newBuilder()
        .setId(id)
        .setOrderId(ORDER_ID)
        .setFromStatus(FROM_STATUS.toString())
        .setToStatus(StringValue.of(TO_STATUS.toString()))
        .setFromTime(FROM_TIME.getEpochSecond())
        .setToTime(Int64Value.of(TO_TIME.getEpochSecond()))
        .setEtlStatus(ETL_STATUS.toString())
        .setEntityStatus(ENTITY_STATUS.toString())
        .setCreatedBy((CREATED_BY))
        .setCreatedDate(CREATED_DATE.getEpochSecond())
        .setUpdatedBy(UPDATED_BY)
        .setUpdatedDate(UPDATED_DATE.getEpochSecond())
        .build();
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistoryWithOutToStatusAndToTime(
      String id) {
    return MongoOrderStatusHistory.newBuilder()
        .setId(id)
        .setOrderId(ORDER_ID)
        .setFromStatus(FROM_STATUS.toString())
        .setFromTime(FROM_TIME.getEpochSecond())
        .setEtlStatus(ETL_STATUS.toString())
        .setEntityStatus(ENTITY_STATUS.toString())
        .setCreatedBy((CREATED_BY))
        .setCreatedDate(CREATED_DATE.getEpochSecond())
        .setUpdatedBy(UPDATED_BY)
        .setUpdatedDate(UPDATED_DATE.getEpochSecond())
        .build();
  }
}
