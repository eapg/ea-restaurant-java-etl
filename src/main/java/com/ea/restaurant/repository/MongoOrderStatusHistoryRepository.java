package com.ea.restaurant.repository;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoOrderStatusHistoryRepository
    extends MongoRepository<MongoOrderStatusHistory, String> {
  List<MongoOrderStatusHistory> findAllByEtlStatus(EtlStatus etlStatus);

  List<MongoOrderStatusHistory> findAllByIdIn(List<ObjectId> ids);
}
