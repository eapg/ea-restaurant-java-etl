package com.ea.restaurant.util;

import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.Date;

public class OrderStatusHistoryMapper {
  public static OrderStatusHistory mapMongoOrderStatusToPostgresqlOrderStatus(
      MongoOrderStatusHistory mongoOrderStatusHistory) {
    var orderStatusHistory = new OrderStatusHistory();
    orderStatusHistory.setMongoOrderStatusHistoryUuid(mongoOrderStatusHistory.getId().toString());
    orderStatusHistory.setOrderId(mongoOrderStatusHistory.getOrderId());
    orderStatusHistory.setEtlStatus(mongoOrderStatusHistory.getEtlStatus());
    orderStatusHistory.setFromStatus(mongoOrderStatusHistory.getFromStatus());
    orderStatusHistory.setToStatus(mongoOrderStatusHistory.getToStatus());
    if (mongoOrderStatusHistory.getToTime() != null) {
      orderStatusHistory.setToTime(
          Date.from(
              DateTimeUtil.getUtcFromInstant(mongoOrderStatusHistory.getToTime().toInstant())));
    }
    orderStatusHistory.setFromTime(
        Date.from(
            DateTimeUtil.getUtcFromInstant(mongoOrderStatusHistory.getFromTime().toInstant())));
    orderStatusHistory.setEntityStatus(mongoOrderStatusHistory.getEntityStatus());
    orderStatusHistory.setCreatedBy(mongoOrderStatusHistory.getCreatedBy());
    orderStatusHistory.setUpdatedBy(mongoOrderStatusHistory.getUpdatedBy());
    orderStatusHistory.setCreatedDate(
        Date.from(
            DateTimeUtil.getUtcFromInstant(mongoOrderStatusHistory.getCreatedDate().toInstant())));
    orderStatusHistory.setUpdatedDate(
        Date.from(
            DateTimeUtil.getUtcFromInstant(mongoOrderStatusHistory.getUpdatedDate().toInstant())));
    return orderStatusHistory;
  }
}
