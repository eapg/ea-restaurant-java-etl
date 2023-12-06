package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MongoOrderStatusHistoryServiceImpl implements MongoOrderStatusHistoryService {

  private final MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;

  public MongoOrderStatusHistoryServiceImpl(
      MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository) {
    this.mongoOrderStatusHistoryRepository = mongoOrderStatusHistoryRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public List<MongoOrderStatusHistory> findUnProcessedOrderStatusHistories() {
    return this.mongoOrderStatusHistoryRepository.findAllByEtlStatus(EtlStatus.UNPROCESSED);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateBatchToProcessed(Set<ObjectId> ids) {
    var mongoOrderStatusHistories = this.mongoOrderStatusHistoryRepository.findAllByIdIn(ids);
    for (MongoOrderStatusHistory mongoOrderStatusHistory : mongoOrderStatusHistories) {
      mongoOrderStatusHistory.setEtlStatus(EtlStatus.PROCESSED);
    }
    this.mongoOrderStatusHistoryRepository.saveAll(mongoOrderStatusHistories);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public List<MongoOrderStatusHistory> insertMongoOrderStatusHistories(
      List<MongoOrderStatusHistory> mongoOrderStatusHistories) {
    return this.mongoOrderStatusHistoryRepository.saveAll(mongoOrderStatusHistories);
  }
}
