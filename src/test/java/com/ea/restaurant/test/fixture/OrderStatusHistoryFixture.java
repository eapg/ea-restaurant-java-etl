package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.OrderStatusHistory;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class OrderStatusHistoryFixture {
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

  public static OrderStatusHistory buildOrderStatusHistory(
      OrderStatusHistory orderStatusHistoryExample) {
    var orderStatusHistory = new OrderStatusHistory();
    orderStatusHistory.setId(orderStatusHistoryExample.getId());
    orderStatusHistory.setOrderId(
        Optional.ofNullable(orderStatusHistoryExample.getOrderId()).orElse(ORDER_ID));
    orderStatusHistory.setFromTime(
        Optional.ofNullable(orderStatusHistoryExample.getFromTime()).orElse(Date.from(FROM_TIME)));
    orderStatusHistory.setToTime(
        Optional.ofNullable(orderStatusHistoryExample.getToTime()).orElse(Date.from(TO_TIME)));
    orderStatusHistory.setFromStatus(
        Optional.ofNullable(orderStatusHistoryExample.getFromStatus()).orElse(FROM_STATUS));
    orderStatusHistory.setToStatus(
        Optional.ofNullable(orderStatusHistoryExample.getToStatus()).orElse(TO_STATUS));
    orderStatusHistory.setMongoOrderStatusHistoryUuid(
        Optional.ofNullable(orderStatusHistoryExample.getMongoOrderStatusHistoryUuid())
            .orElse(MONGODB_ORDER_STATUS_HISTORY_UUID));
    orderStatusHistory.setEtlStatus(
        Optional.ofNullable(orderStatusHistoryExample.getEtlStatus()).orElse(ETL_STATUS));
    orderStatusHistory.setEntityStatus(
        Optional.ofNullable(orderStatusHistoryExample.getEntityStatus()).orElse(ENTITY_STATUS));
    orderStatusHistory.setCreatedDate(
        Optional.ofNullable(orderStatusHistoryExample.getCreatedDate())
            .orElse(Date.from(CREATED_DATE)));
    orderStatusHistory.setCreatedBy(
        Optional.ofNullable(orderStatusHistoryExample.getCreatedBy()).orElse(CREATED_BY));
    orderStatusHistory.setUpdatedBy(
        Optional.ofNullable(orderStatusHistoryExample.getUpdatedBy()).orElse(UPDATED_BY));
    orderStatusHistory.setUpdatedDate(
        Optional.ofNullable(orderStatusHistoryExample.getUpdatedDate())
            .orElse(Date.from(UPDATED_DATE)));
    return orderStatusHistory;
  }

  public static OrderStatusHistory buildOrderStatusHistory(
      Long id, Long orderId, OrderStatus fromStatus, Instant fromTime) {
    var orderStatusHistoryExample = new OrderStatusHistory(id);
    orderStatusHistoryExample.setOrderId(orderId);
    orderStatusHistoryExample.setFromStatus(fromStatus);
    orderStatusHistoryExample.setFromTime(Date.from(fromTime));

    return buildOrderStatusHistory(orderStatusHistoryExample);
  }

  public static OrderStatusHistory buildOrderStatusHistory(Long id) {
    var orderStatusHistoryExample = new OrderStatusHistory(id);
    return buildOrderStatusHistory(orderStatusHistoryExample);
  }

  public static OrderStatusHistory buildOrderStatusHistory() {
    var orderStatusHistoryExample = new OrderStatusHistory();
    return buildOrderStatusHistory(orderStatusHistoryExample);
  }
}
