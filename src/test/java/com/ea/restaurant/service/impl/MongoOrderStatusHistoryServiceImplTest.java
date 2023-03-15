package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;
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
    var orderStatusHistory1 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    orderStatusHistory1.setEtlStatus(EtlStatus.UNPROCESSED);
    var orderStatusHistory2 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31ca"));
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

  @Test
  public void whenReceiveListOfUuid_shouldUpdateThoseOrderStatusToProcessed() {
    var mongoIds =
        Set.of(new ObjectId("63656f20f2a8a6a247ae31cb"), new ObjectId("63656f20f2a8a6a247ae31cd"));
    var mongoOrderStatusHistory1 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var mongoOrderStatusHistory2 = MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory();
    var mongoOrderStatusHistoriesExpected =
        List.of(mongoOrderStatusHistory1, mongoOrderStatusHistory2);
    Mockito.when(this.mongoOrderStatusHistoryRepository.findAllByIdIn(Mockito.eq(mongoIds)))
        .thenReturn(mongoOrderStatusHistoriesExpected);
    this.mongoOrderStatusHistoryService.updateBatchToProcessed(mongoIds);

    Mockito.verify(this.mongoOrderStatusHistoryRepository, Mockito.times(1))
        .findAllByIdIn(mongoIds);
    Mockito.verify(this.mongoOrderStatusHistoryRepository, Mockito.times(1))
        .saveAll(Mockito.eq(mongoOrderStatusHistoriesExpected));
  }
}
