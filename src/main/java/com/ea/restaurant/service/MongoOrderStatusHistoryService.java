package com.ea.restaurant.service;

import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.util.List;
import org.bson.types.ObjectId;

public interface MongoOrderStatusHistoryService {
  List<MongoOrderStatusHistory> findUnProcessedOrderStatusHistories();

  void updateBatchToProcessed(List<ObjectId> ids);
}
