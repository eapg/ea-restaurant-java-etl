package com.ea.restaurant.config;

import com.ea.restaurant.etl.impl.MongoToPostgresqlOrderStatusHistoryEtlService;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class SchedulerTest {

  private MongoToPostgresqlOrderStatusHistoryEtlService
      mongoToPostgresqlOrderStatusHistoryEtlService;
  private Scheduler scheduler;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  void setUp() {
    mongoToPostgresqlOrderStatusHistoryEtlService =
        Mockito.mock(MongoToPostgresqlOrderStatusHistoryEtlService.class);
    scheduler = new Scheduler(mongoToPostgresqlOrderStatusHistoryEtlService);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  void runMongoToPostgresqlEtl() {
    var unProcessedOrderStatus1 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatus2 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    var unProcessedOrderStatusHistories = List.of(unProcessedOrderStatus1, unProcessedOrderStatus2);
    Mockito.when(mongoToPostgresqlOrderStatusHistoryEtlService.extract())
        .thenReturn(unProcessedOrderStatusHistories);

    this.scheduler.runMongoToPostgresqlEtl();

    Mockito.verify(mongoToPostgresqlOrderStatusHistoryEtlService, Mockito.times(1)).extract();
    Mockito.verify(mongoToPostgresqlOrderStatusHistoryEtlService, Mockito.times(1))
        .transform(Mockito.any());
    Mockito.verify(mongoToPostgresqlOrderStatusHistoryEtlService, Mockito.times(1))
        .load(Mockito.any());
  }
}
