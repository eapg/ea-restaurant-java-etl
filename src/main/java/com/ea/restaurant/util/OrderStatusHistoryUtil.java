package com.ea.restaurant.util;

import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusHistoryUtil {
  public static List<OrderStatusHistory> updateLastOrderStatusHistory(
      List<OrderStatusHistory> lastOrderStatusHistoriesInBd,
      List<OrderStatusHistory> lastOrderStatusHistory,
      Long updatedBy) {
    var updatedOrderStatusHistories = new ArrayList<OrderStatusHistory>();

    for (OrderStatusHistory orderStatusHistory : lastOrderStatusHistory) {

      if (orderStatusHistory.getFromStatus() != OrderStatus.NEW_ORDER) {
        var orderStatusHistoryToBeUpdated =
            lastOrderStatusHistoriesInBd.stream()
                .filter(
                    orderStatusHistoryFromBd ->
                        orderStatusHistoryFromBd
                            .getOrderId()
                            .equals(orderStatusHistory.getOrderId()))
                .findAny();
        if (orderStatusHistoryToBeUpdated.isPresent()) {
          var orderStatusHistoryToBeUpdatedUnWrapped = orderStatusHistoryToBeUpdated.get();
          orderStatusHistoryToBeUpdatedUnWrapped.setToStatus(orderStatusHistory.getFromStatus());
          orderStatusHistoryToBeUpdatedUnWrapped.setToTime(orderStatusHistory.getFromTime());
          orderStatusHistoryToBeUpdatedUnWrapped.setUpdatedBy(updatedBy);
          updatedOrderStatusHistories.add(orderStatusHistoryToBeUpdatedUnWrapped);
        }
      }
    }
    return updatedOrderStatusHistories;
  }
}
