package com.ea.restaurant.etl.impl;

import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.UserType;
import com.ea.restaurant.entities.OrderStatusHistory;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.service.OrderStatusHistoryService;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import com.ea.restaurant.test.fixture.OrderStatusHistoryFixture;
import com.ea.restaurant.util.OrderStatusHistoryUtil;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class MongoToPostgresqlOrderStatusHistoryEtlServiceTest {
  private MongoToPostgresqlOrderStatusHistoryEtlService mongoToPostgresqlOrderStatusHistoryService;
  private OrderStatusHistoryService orderStatusHistoryService;
  private MongoOrderStatusHistoryService mongoOrderStatusHistoryService;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  void setUp() {
    orderStatusHistoryService = Mockito.mock(OrderStatusHistoryService.class);
    mongoOrderStatusHistoryService = Mockito.mock(MongoOrderStatusHistoryService.class);
    mongoToPostgresqlOrderStatusHistoryService =
        new MongoToPostgresqlOrderStatusHistoryEtlService(
            orderStatusHistoryService, mongoOrderStatusHistoryService);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  void whenExtract_shouldFindUnprocessedOrderStatusHistories() {
    var unProcessedOrderStatus1 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var unProcessedOrderStatus2 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var unProcessedOrderStatusHistories = List.of(unProcessedOrderStatus1, unProcessedOrderStatus2);

    Mockito.when(this.mongoOrderStatusHistoryService.findUnProcessedOrderStatusHistories())
        .thenReturn(unProcessedOrderStatusHistories);

    var unProcessedOrderStatusHistoriesReturned =
        this.mongoToPostgresqlOrderStatusHistoryService.extract();

    Assertions.assertEquals(
        unProcessedOrderStatusHistories, unProcessedOrderStatusHistoriesReturned);
    Mockito.verify(this.mongoOrderStatusHistoryService, Mockito.times(1))
        .findUnProcessedOrderStatusHistories();
  }

  @Test
  void whenTransform_shouldTransformMongoOrderStatusToPostgresqlOrderStatus() {
    var unProcessedOrderStatus1 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatus2 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatusHistories = List.of(unProcessedOrderStatus1, unProcessedOrderStatus2);

    var transformedOrderStatusHistory =
        this.mongoToPostgresqlOrderStatusHistoryService.transform(unProcessedOrderStatusHistories);
    Assertions.assertEquals(
        OrderStatusHistory.class, transformedOrderStatusHistory.get(0).getClass());
    Assertions.assertEquals(
        unProcessedOrderStatus1.getOrderId(), transformedOrderStatusHistory.get(0).getOrderId());
  }

  @Test
  void whenLoad_shouldInsertNewOrUpdateBatchOrderStatusHistories() {
    var orderStatusHistory1 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory1.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cb");
    orderStatusHistory1.setFromStatus(OrderStatus.CANCELLED);
    var orderStatusHistory2 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory2.setOrderId(2L);
    orderStatusHistory2.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cc");
    orderStatusHistory2.setFromStatus(OrderStatus.CANCELLED);

    var orderStatusHistory3 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory3.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cb");
    var orderStatusHistory4 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory4.setOrderId(2L);
    orderStatusHistory4.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cc");

    var orderStatusHistory5 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory5.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cb");
    orderStatusHistory5.setToStatus(OrderStatus.CANCELLED);
    var orderStatusHistory6 = OrderStatusHistoryFixture.buildOrderStatusHistory();
    orderStatusHistory6.setMongodbOrderStatusHistoryUuid("63656f20f2a8a6a247ae31cc");
    orderStatusHistory6.setOrderId(2L);
    orderStatusHistory6.setToStatus(OrderStatus.CANCELLED);

    var orderIds = List.of(orderStatusHistory1.getOrderId(), orderStatusHistory2.getOrderId());
    var transformedOrderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);
    var lastOrderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);
    var updatedOrderStatusHistories = List.of(orderStatusHistory3, orderStatusHistory4);

    Mockito.when(
            this.orderStatusHistoryService.findLastOrderStatusHistoriesByOrderIds(
                Mockito.eq(orderIds)))
        .thenReturn(lastOrderStatusHistories);

    Mockito.when(
            OrderStatusHistoryUtil.updateLastOrderStatusHistory(
                Mockito.eq(lastOrderStatusHistories),
                Mockito.eq(transformedOrderStatusHistories),
                Mockito.eq(UserType.INTERNAL.getId())))
        .thenReturn(updatedOrderStatusHistories);

    var mockedOrderStatusHistoryUtil = Mockito.mockStatic(OrderStatusHistoryUtil.class);

    this.mongoToPostgresqlOrderStatusHistoryService.load(transformedOrderStatusHistories);
    Mockito.verify(this.orderStatusHistoryService, Mockito.times(1))
        .findLastOrderStatusHistoriesByOrderIds(Mockito.eq(orderIds));
    mockedOrderStatusHistoryUtil.verify(
        () ->
            OrderStatusHistoryUtil.updateLastOrderStatusHistory(
                Mockito.eq(lastOrderStatusHistories),
                Mockito.eq(transformedOrderStatusHistories),
                Mockito.eq(UserType.INTERNAL.getId())));
  }
}
