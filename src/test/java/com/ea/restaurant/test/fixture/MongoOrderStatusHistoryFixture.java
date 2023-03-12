package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.time.Instant;
import java.util.Optional;
import org.bson.types.ObjectId;

public class MongoOrderStatusHistoryFixture {
  public static final Long ORDER_ID = 1L;
  public static final Instant FROM_TIME = Instant.EPOCH;
  public static final Instant TO_TIME = Instant.EPOCH;
  public static final OrderStatus FROM_STATUS = OrderStatus.NEW_ORDER;
  public static final OrderStatus TO_STATUS = OrderStatus.IN_PROCESS;
  public static final String MONGODB_ORDER_STATUS_HISTORY_UUID = "MONGOUUID";
  public static final EtlStatus ETL_STATUS = EtlStatus.PROCESSED;
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(
      MongoOrderStatusHistory MongoOrderStatusHistoryExample) {
    var MongoOrderStatusHistory = new MongoOrderStatusHistory();
    MongoOrderStatusHistory.setId(MongoOrderStatusHistoryExample.getId());
    MongoOrderStatusHistory.setOrderId(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getOrderId()).orElse(ORDER_ID));
    MongoOrderStatusHistory.setFromTime(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getFromTime()).orElse(FROM_TIME));
    MongoOrderStatusHistory.setToTime(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getToTime()).orElse(TO_TIME));
    MongoOrderStatusHistory.setFromStatus(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getFromStatus()).orElse(FROM_STATUS));
    MongoOrderStatusHistory.setToStatus(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getToStatus()).orElse(TO_STATUS));
    MongoOrderStatusHistory.setEtlStatus(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getEtlStatus()).orElse(ETL_STATUS));
    MongoOrderStatusHistory.setEntityStatus(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getEntityStatus())
            .orElse(ENTITY_STATUS));
    MongoOrderStatusHistory.setCreatedDate(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getCreatedDate()).orElse(CREATED_DATE));
    MongoOrderStatusHistory.setCreatedBy(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getCreatedBy()).orElse(CREATED_BY));
    MongoOrderStatusHistory.setUpdatedBy(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getUpdatedBy()).orElse(UPDATED_BY));
    MongoOrderStatusHistory.setUpdatedDate(
        Optional.ofNullable(MongoOrderStatusHistoryExample.getUpdatedDate()).orElse(UPDATED_DATE));
    return MongoOrderStatusHistory;
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(
      ObjectId id, Long orderId, OrderStatus fromStatus, Instant fromTime) {
    var MongoOrderStatusHistoryExample = new MongoOrderStatusHistory(id);
    MongoOrderStatusHistoryExample.setOrderId(orderId);
    MongoOrderStatusHistoryExample.setFromStatus(fromStatus);
    MongoOrderStatusHistoryExample.setFromTime(fromTime);

    return buildMongoOrderStatusHistory(MongoOrderStatusHistoryExample);
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(ObjectId id) {
    var MongoOrderStatusHistoryExample = new MongoOrderStatusHistory(id);
    return buildMongoOrderStatusHistory(MongoOrderStatusHistoryExample);
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory() {
    var MongoOrderStatusHistoryExample = new MongoOrderStatusHistory();
    return buildMongoOrderStatusHistory(MongoOrderStatusHistoryExample);
  }
}
