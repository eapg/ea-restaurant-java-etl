package com.ea.restaurant.etl.impl;

import com.ea.restaurant.constants.UserType;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.entities.OrderStatusHistory;
import com.ea.restaurant.etl.Etl;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.service.OrderStatusHistoryService;
import com.ea.restaurant.util.OrderStatusHistoryMapper;
import com.ea.restaurant.util.OrderStatusHistoryUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class MongoToPostgresqlOrderStatusHistoryEtlService
    implements Etl<MongoOrderStatusHistory, OrderStatusHistory> {

  private final OrderStatusHistoryService orderStatusHistoryService;
  private final MongoOrderStatusHistoryService mongoOrderStatusHistoryService;

  public MongoToPostgresqlOrderStatusHistoryEtlService(
      OrderStatusHistoryService orderStatusHistoryService,
      MongoOrderStatusHistoryService mongoOrderStatusHistoryService) {
    this.orderStatusHistoryService = orderStatusHistoryService;
    this.mongoOrderStatusHistoryService = mongoOrderStatusHistoryService;
  }

  @Override
  public List<MongoOrderStatusHistory> extract() {
    return this.mongoOrderStatusHistoryService.findUnProcessedOrderStatusHistories();
  }

  @Override
  public List<OrderStatusHistory> transform(List<MongoOrderStatusHistory> extractedData) {
    return extractedData.stream()
        .map(OrderStatusHistoryMapper::mapMongoOrderStatusToPostgresqlOrderStatus)
        .toList();
  }

  @Override
  public void load(List<OrderStatusHistory> transformedData) {
    var mongoUuids = new HashSet<ObjectId>();
    var orderIds = new ArrayList<Long>();
    for (OrderStatusHistory orderStatusHistory : transformedData) {
      mongoUuids.add(new ObjectId(orderStatusHistory.getMongoOrderStatusHistoryUuid()));

      if (!orderIds.contains(orderStatusHistory.getOrderId())) {
        orderIds.add(orderStatusHistory.getOrderId());
      }
    }
    var lastOrderStatusHistories =
        this.orderStatusHistoryService.findLastOrderStatusHistoriesByOrderIds(orderIds);

    if (!lastOrderStatusHistories.isEmpty()) {
      var updatedOrderStatusHistories =
          OrderStatusHistoryUtil.updateLastOrderStatusHistory(
              lastOrderStatusHistories, transformedData, UserType.INTERNAL.getId());
      this.orderStatusHistoryService.insertNewOrUpdatedBatchOrderStatusHistories(
          updatedOrderStatusHistories);
    }
    this.orderStatusHistoryService.insertNewOrUpdatedBatchOrderStatusHistories(transformedData);
    this.mongoOrderStatusHistoryService.updateBatchToProcessed(mongoUuids);
  }
}
