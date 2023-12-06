package com.ea.restaurant.etl.impl;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.entities.OrderStatusHistory;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.repository.OrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.service.OrderStatusHistoryService;
import com.ea.restaurant.service.impl.MongoOrderStatusHistoryServiceImpl;
import com.ea.restaurant.service.impl.OrderStatusHistoryServiceImpl;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import com.ea.restaurant.test.fixture.OrderStatusHistoryFixture;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MongoToPostgresqlOrderStatusHistoryEtlServiceIntegrationTest {

  private OrderStatusHistoryRepository orderStatusHistoryRepository;
  private MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;
  private OrderStatusHistoryService orderStatusHistoryService;
  private MongoOrderStatusHistoryService mongoOrderStatusHistoryService;
  private MongoToPostgresqlOrderStatusHistoryEtlService
      mongoToPostgresqlOrderStatusHistoryEtlService;

  @BeforeEach
  public void setUp() {
    orderStatusHistoryRepository = Mockito.mock(OrderStatusHistoryRepository.class);
    mongoOrderStatusHistoryRepository = Mockito.mock(MongoOrderStatusHistoryRepository.class);
    orderStatusHistoryService =
        Mockito.spy(new OrderStatusHistoryServiceImpl(orderStatusHistoryRepository));
    mongoOrderStatusHistoryService =
        Mockito.spy(new MongoOrderStatusHistoryServiceImpl(mongoOrderStatusHistoryRepository));
    mongoToPostgresqlOrderStatusHistoryEtlService =
        new MongoToPostgresqlOrderStatusHistoryEtlService(
            orderStatusHistoryService, mongoOrderStatusHistoryService);
  }

  @Test
  public void whenExtract_shouldFindUnprocessedOrderStatusHistories() {
    var unProcessedOrderStatus1 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var unProcessedOrderStatus2 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var unProcessedOrderStatusHistories = List.of(unProcessedOrderStatus1, unProcessedOrderStatus2);

    Mockito.when(
            this.mongoOrderStatusHistoryRepository.findAllByEtlStatus(
                Mockito.eq(EtlStatus.UNPROCESSED)))
        .thenReturn(unProcessedOrderStatusHistories);

    var unProcessedOrderStatusHistoriesReturned =
        this.mongoToPostgresqlOrderStatusHistoryEtlService.extract();

    Assertions.assertEquals(
        unProcessedOrderStatusHistories, unProcessedOrderStatusHistoriesReturned);

    Mockito.verify(this.mongoOrderStatusHistoryRepository, Mockito.times(1))
        .findAllByEtlStatus(Mockito.eq(EtlStatus.UNPROCESSED));
    Mockito.verify(this.mongoOrderStatusHistoryService, Mockito.times(1))
        .findUnProcessedOrderStatusHistories();
  }

  @Test
  public void whenTransform_shouldTransformMongoOrderStatusHistoryToPostgresqlOrderStatusHistory() {
    var unProcessedOrderStatus1 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatus2 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatusHistories = List.of(unProcessedOrderStatus1, unProcessedOrderStatus2);

    var transformedOrderStatusHistory =
        this.mongoToPostgresqlOrderStatusHistoryEtlService.transform(
            unProcessedOrderStatusHistories);
    Assertions.assertEquals(
        OrderStatusHistory.class, transformedOrderStatusHistory.get(0).getClass());
    Assertions.assertEquals(
        unProcessedOrderStatus1.getOrderId(), transformedOrderStatusHistory.get(0).getOrderId());
  }

  @Test
  public void whenLoad_shouldLoadTransformedOrderStatusIntoDb() {
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

    var orderIds = List.of(orderStatusHistory1.getOrderId(), orderStatusHistory2.getOrderId());
    var transformedOrderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);
    var lastOrderStatusHistories = List.of(orderStatusHistory3, orderStatusHistory4);

    Mockito.when(
            this.orderStatusHistoryRepository.findLastOrderStatusHistoriesByOrderIds(
                Mockito.eq(orderIds)))
        .thenReturn(lastOrderStatusHistories);

    this.mongoToPostgresqlOrderStatusHistoryEtlService.load(transformedOrderStatusHistories);
    Mockito.verify(this.orderStatusHistoryRepository, Mockito.times(1))
        .findLastOrderStatusHistoriesByOrderIds(Mockito.eq(orderIds));

    Mockito.verify(this.orderStatusHistoryService, Mockito.times(1))
        .findLastOrderStatusHistoriesByOrderIds(Mockito.eq(orderIds));

    Mockito.verify(this.orderStatusHistoryService, Mockito.times(2))
        .insertNewOrUpdatedBatchOrderStatusHistories(Mockito.any());
  }
}
