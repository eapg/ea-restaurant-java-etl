package com.ea.restaurant.util;

import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.entities.OrderStatusHistory;

public class OrderStatusHistoryMapper {
  public static OrderStatusHistory mapMongoOrderStatusToPostgresqlOrderStatus(
      MongoOrderStatusHistory mongoOrderStatusHistory) {
    var orderStatusHistory = new OrderStatusHistory();
    orderStatusHistory.setMongoOrderStatusHistoryUuid(mongoOrderStatusHistory.getId().toString());
    orderStatusHistory.setOrderId(mongoOrderStatusHistory.getOrderId());
    orderStatusHistory.setEtlStatus(mongoOrderStatusHistory.getEtlStatus());
    orderStatusHistory.setFromStatus(mongoOrderStatusHistory.getFromStatus());
    orderStatusHistory.setToStatus(mongoOrderStatusHistory.getToStatus());
    orderStatusHistory.setFromTime(mongoOrderStatusHistory.getFromTime());
    orderStatusHistory.setToTime(mongoOrderStatusHistory.getToTime());
    orderStatusHistory.setEntityStatus(mongoOrderStatusHistory.getEntityStatus());
    orderStatusHistory.setCreatedBy(mongoOrderStatusHistory.getCreatedBy());
    orderStatusHistory.setUpdatedBy(mongoOrderStatusHistory.getUpdatedBy());
    orderStatusHistory.setCreatedDate(mongoOrderStatusHistory.getCreatedDate());
    orderStatusHistory.setUpdatedDate(mongoOrderStatusHistory.getUpdatedDate());
    return orderStatusHistory;
  }
}
