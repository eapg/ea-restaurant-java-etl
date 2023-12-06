package com.ea.restaurant.service;

import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;

public interface MongoOrderStatusHistoryService {
  List<MongoOrderStatusHistory> findUnProcessedOrderStatusHistories();

  void updateBatchToProcessed(Set<ObjectId> ids);

  List<MongoOrderStatusHistory> insertMongoOrderStatusHistories(
      List<MongoOrderStatusHistory> mongoOrderStatusHistories);
}
