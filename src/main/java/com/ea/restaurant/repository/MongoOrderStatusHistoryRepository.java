package com.ea.restaurant.repository;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.entities.OrderStatusHistory;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoOrderStatusHistoryRepository
    extends MongoRepository<OrderStatusHistory, Long> {
  List<OrderStatusHistory> findAllByEtlStatus(EtlStatus etlStatus);
}
