package com.ea.restaurant.service;

import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.List;

public interface OrderStatusHistoryService {
  List<OrderStatusHistory> findLastOrderStatusHistoriesByOrderIds(List<Long> orderIds);

  void insertNewOrUpdatedBatchOrderStatusHistories(List<OrderStatusHistory> orderStatusHistories);
}
