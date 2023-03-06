package com.ea.restaurant.test.fixture;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.OrderStatusHistory;
import java.time.Instant;
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

  public static OrderStatusHistory buildOrderStatusHistory(OrderStatusHistory orderStatusHistory) {
    var orderStatusHistoryExample = new OrderStatusHistory();
    orderStatusHistoryExample.setId(orderStatusHistory.getId());
    orderStatusHistoryExample.setOrderId(
        Optional.ofNullable(orderStatusHistory.getOrderId()).orElse(ORDER_ID));
    orderStatusHistoryExample.setFromTime(
        Optional.ofNullable(orderStatusHistory.getFromTime()).orElse(FROM_TIME));
    orderStatusHistoryExample.setToTime(
        Optional.ofNullable(orderStatusHistory.getToTime()).orElse(TO_TIME));
    orderStatusHistoryExample.setFromStatus(
        Optional.ofNullable(orderStatusHistory.getFromStatus()).orElse(FROM_STATUS));
    orderStatusHistoryExample.setToStatus(
        Optional.ofNullable(orderStatusHistory.getToStatus()).orElse(TO_STATUS));
    orderStatusHistoryExample.setMongodbOrderStatusHistoryUuid(
        Optional.ofNullable(orderStatusHistory.getMongodbOrderStatusHistoryUuid())
            .orElse(MONGODB_ORDER_STATUS_HISTORY_UUID));
    orderStatusHistoryExample.setEtlStatus(
        Optional.ofNullable(orderStatusHistory.getEtlStatus()).orElse(ETL_STATUS));
    orderStatusHistoryExample.setEntityStatus(
        Optional.ofNullable(orderStatusHistory.getEntityStatus()).orElse(ENTITY_STATUS));
    orderStatusHistoryExample.setCreatedDate(
        Optional.ofNullable(orderStatusHistory.getCreatedDate()).orElse(CREATED_DATE));
    orderStatusHistoryExample.setCreatedBy(
        Optional.ofNullable(orderStatusHistory.getCreatedBy()).orElse(CREATED_BY));
    orderStatusHistoryExample.setUpdatedBy(
        Optional.ofNullable(orderStatusHistory.getUpdatedBy()).orElse(UPDATED_BY));
    orderStatusHistoryExample.setUpdatedDate(
        Optional.ofNullable(orderStatusHistory.getUpdatedDate()).orElse(UPDATED_DATE));
    return orderStatusHistoryExample;
  }

  public static OrderStatusHistory buildOrderStatusHistory(
      Long id, Long orderId, OrderStatus fromStatus, Instant fromTime) {
    var orderStatusHistoryExample = new OrderStatusHistory(id);
    orderStatusHistoryExample.setOrderId(orderId);
    orderStatusHistoryExample.setFromStatus(fromStatus);
    orderStatusHistoryExample.setFromTime(fromTime);

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
