package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.bson.types.ObjectId;

public class MongoOrderStatusHistoryFixture {
  public static final Long ORDER_ID = 1L;
  public static final Instant FROM_TIME = Instant.EPOCH;
  public static final Instant TO_TIME = Instant.EPOCH;
  public static final OrderStatus FROM_STATUS = OrderStatus.NEW_ORDER;
  public static final OrderStatus TO_STATUS = OrderStatus.IN_PROCESS;
  public static final String MONGODB_ORDER_STATUS_HISTORY_UUID = "MONGOUUID";
  public static final EtlStatus ETL_STATUS = EtlStatus.UNPROCESSED;
  public static final Long CREATED_BY = 1L;
  public static final Instant CREATED_DATE = Instant.EPOCH;
  public static final Long UPDATED_BY = 1L;
  public static final Instant UPDATED_DATE = Instant.EPOCH;
  public static final Status ENTITY_STATUS = Status.ACTIVE;

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(
      MongoOrderStatusHistory mongoOrderStatusHistoryExample) {
    var mongoOrderStatusHistory = new MongoOrderStatusHistory();
    mongoOrderStatusHistory.setId(mongoOrderStatusHistoryExample.getId());
    mongoOrderStatusHistory.setOrderId(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getOrderId()).orElse(ORDER_ID));
    mongoOrderStatusHistory.setFromTime(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getFromTime())
            .orElse(Date.from(FROM_TIME)));
    mongoOrderStatusHistory.setToTime(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getToTime()).orElse(Date.from(TO_TIME)));
    mongoOrderStatusHistory.setFromStatus(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getFromStatus()).orElse(FROM_STATUS));
    mongoOrderStatusHistory.setToStatus(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getToStatus()).orElse(TO_STATUS));
    mongoOrderStatusHistory.setEtlStatus(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getEtlStatus()).orElse(ETL_STATUS));
    mongoOrderStatusHistory.setEntityStatus(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getEntityStatus())
            .orElse(ENTITY_STATUS));
    mongoOrderStatusHistory.setCreatedDate(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getCreatedDate())
            .orElse(Date.from(CREATED_DATE)));
    mongoOrderStatusHistory.setCreatedBy(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getCreatedBy()).orElse(CREATED_BY));
    mongoOrderStatusHistory.setUpdatedBy(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getUpdatedBy()).orElse(UPDATED_BY));
    mongoOrderStatusHistory.setUpdatedDate(
        Optional.ofNullable(mongoOrderStatusHistoryExample.getUpdatedDate())
            .orElse(Date.from(UPDATED_DATE)));
    return mongoOrderStatusHistory;
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(
      ObjectId id, Long orderId, OrderStatus fromStatus, Instant fromTime) {
    var mongoOrderStatusHistoryExample = new MongoOrderStatusHistory(id);
    mongoOrderStatusHistoryExample.setOrderId(orderId);
    mongoOrderStatusHistoryExample.setFromStatus(fromStatus);
    mongoOrderStatusHistoryExample.setFromTime(Date.from(fromTime));

    return buildMongoOrderStatusHistory(mongoOrderStatusHistoryExample);
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory(ObjectId id) {
    var mongoOrderStatusHistoryExample = new MongoOrderStatusHistory(id);
    return buildMongoOrderStatusHistory(mongoOrderStatusHistoryExample);
  }

  public static MongoOrderStatusHistory buildMongoOrderStatusHistory() {
    var mongoOrderStatusHistoryExample = new MongoOrderStatusHistory();
    return buildMongoOrderStatusHistory(mongoOrderStatusHistoryExample);
  }
}
