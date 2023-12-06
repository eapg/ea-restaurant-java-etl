package com.ea.restaurant.service.impl;

import com.ea.restaurant.entities.OrderStatusHistory;
import com.ea.restaurant.repository.OrderStatusHistoryRepository;
import com.ea.restaurant.service.OrderStatusHistoryService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

  private final OrderStatusHistoryRepository orderStatusHistoryRepository;

  public OrderStatusHistoryServiceImpl(OrderStatusHistoryRepository orderStatusHistoryRepository) {
    this.orderStatusHistoryRepository = orderStatusHistoryRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public List<OrderStatusHistory> findLastOrderStatusHistoriesByOrderIds(List<Long> orderIds) {
    return this.orderStatusHistoryRepository.findLastOrderStatusHistoriesByOrderIds(orderIds);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void insertNewOrUpdatedBatchOrderStatusHistories(
      List<OrderStatusHistory> orderStatusHistories) {
    this.orderStatusHistoryRepository.saveAll(orderStatusHistories);
  }
}
