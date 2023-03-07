package com.ea.restaurant.service;

import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.List;

public interface MongoOrderStatusHistoryService {
  List<OrderStatusHistory> findUnProcessedOrderStatusHistories();
}
