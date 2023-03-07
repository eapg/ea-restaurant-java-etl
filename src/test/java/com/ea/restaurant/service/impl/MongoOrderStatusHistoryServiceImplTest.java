package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.test.fixture.OrderStatusHistoryFixture;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MongoOrderStatusHistoryServiceImplTest {

  private MongoOrderStatusHistoryService mongoOrderStatusHistoryService;
  private MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  public void setUp() throws Exception {
    mongoOrderStatusHistoryRepository = Mockito.mock(MongoOrderStatusHistoryRepository.class);
    mongoOrderStatusHistoryService =
        new MongoOrderStatusHistoryServiceImpl(mongoOrderStatusHistoryRepository);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  public void findUnProcessedOrderStatusHistories() {
    var orderStatusHistory1 = OrderStatusHistoryFixture.buildOrderStatusHistory(1L);
    orderStatusHistory1.setEtlStatus(EtlStatus.UNPROCESSED);
    var orderStatusHistory2 = OrderStatusHistoryFixture.buildOrderStatusHistory(2L);
    orderStatusHistory2.setEtlStatus(EtlStatus.UNPROCESSED);
    var orderStatusHistories = List.of(orderStatusHistory1, orderStatusHistory2);

    Mockito.when(
            this.mongoOrderStatusHistoryRepository.findAllByEtlStatus(
                Mockito.eq(EtlStatus.UNPROCESSED)))
        .thenReturn(orderStatusHistories);

    var unprocessedMongoOrderStatusHistories =
        this.mongoOrderStatusHistoryService.findUnProcessedOrderStatusHistories();
    Assertions.assertEquals(orderStatusHistories, unprocessedMongoOrderStatusHistories);
    Mockito.verify(this.mongoOrderStatusHistoryRepository, Mockito.times(1))
        .findAllByEtlStatus(Mockito.eq(EtlStatus.UNPROCESSED));
  }
}
