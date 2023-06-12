package com.ea.restaurant.util;

import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.UserType;
import com.ea.restaurant.test.fixture.OrderStatusHistoryFixture;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusHistoryUtilTest {

  @Test
  void whenUpdateLastOrderStatusHistory_shouldUpdateToStatusInLastOrderStatusHistory() {
    var orderStatusHistory1 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory1.setMongoOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cb");
    orderStatusHistory1.setFromStatus(OrderStatus.CANCELLED);
    var orderStatusHistory2 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory2.setOrderId(2L);
    orderStatusHistory2.setMongoOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cc");
    orderStatusHistory2.setFromStatus(OrderStatus.CANCELLED);

    var orderStatusHistory3 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory3.setMongoOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cb");
    var orderStatusHistory4 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory4.setOrderId(2L);
    orderStatusHistory4.setMongoOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cc");

    var transformedOrderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);
    var lastOrderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);

    var updatedLastOrderStatusHistories =
        OrderStatusHistoryUtil.updateLastOrderStatusHistory(
            lastOrderStatusHistories, transformedOrderStatusHistories, UserType.INTERNAL.getId());

    Assertions.assertEquals(
        updatedLastOrderStatusHistories.get(1).getToStatus(),
        transformedOrderStatusHistories.get(1).getFromStatus());
  }
}
