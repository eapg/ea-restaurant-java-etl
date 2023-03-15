package com.ea.restaurant.util;

import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusHistoryMapperTest {

  @Test
  void
      whenMapMongoOrderStatusToPostgresqlOrderStatus_shouldMapMongoOrderStatusToOrderStatus() {
    var mongoOrderStatusHistory =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cc"));

    var orderStatusHistory =
        OrderStatusHistoryMapper.mapMongoOrderStatusToPostgresqlOrderStatus(
            mongoOrderStatusHistory);

    Assertions.assertEquals(
        mongoOrderStatusHistory.getId().toString(),
        orderStatusHistory.getMongodbOrderStatusHistoryUuid());

    Assertions.assertEquals(mongoOrderStatusHistory.getOrderId(), orderStatusHistory.getOrderId());
  }
}
