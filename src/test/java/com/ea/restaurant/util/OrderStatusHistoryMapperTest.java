package com.ea.restaurant.util;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.ea.restaurant.util.OrderStatusHistoryMapper.mapMongoOrderStatusToPostgresqlOrderStatus;

class OrderStatusHistoryMapperTest {

  @Test
  void whenMapMongoOrderStatusToPostgresqlOrderStatus_shouldMapMongoOrderStatusToOrderStatus() {
    var mongoOrderStatusHistory =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cc"));

    var orderStatusHistory =
        OrderStatusHistoryMapper.mapMongoOrderStatusToPostgresqlOrderStatus(
            mongoOrderStatusHistory);

    Assertions.assertEquals(
        mongoOrderStatusHistory.getId().toString(),
        orderStatusHistory.getMongoOrderStatusHistoryUuid());

    Assertions.assertEquals(mongoOrderStatusHistory.getOrderId(), orderStatusHistory.getOrderId());
  }
}
