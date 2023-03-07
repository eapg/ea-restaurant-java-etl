package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.entities.OrderStatusHistory;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MongoOrderStatusHistoryServiceImpl implements MongoOrderStatusHistoryService {

  private final MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;

  public MongoOrderStatusHistoryServiceImpl(
      MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository) {
    this.mongoOrderStatusHistoryRepository = mongoOrderStatusHistoryRepository;
  }

  @Override
  public List<OrderStatusHistory> findUnProcessedOrderStatusHistories() {
    return this.mongoOrderStatusHistoryRepository.findAllByEtlStatus(EtlStatus.UNPROCESSED);
  }
}
