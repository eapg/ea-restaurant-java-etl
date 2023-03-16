package com.ea.restaurant.grpc;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.service.impl.MongoOrderStatusHistoryServiceImpl;
import com.ea.restaurant.test.fixture.MongoOrderStatusHistoryFixture;
import com.ea.restaurant.test.util.GrpcTestUtil;
import com.ea.restaurant.util.GrpcMongoOrderStatusHistoryMapper;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GrpcMongoOrderStatusHistoryServiceImplIntegrationTest {
  private MongoOrderStatusHistoryService mongoOrderStatusHistoryService;
  private GrpcMongoOrderStatusHistoryServiceImpl grpcMongoOrderStatusHistoryService;
  private MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  public void setUp() {
    mongoOrderStatusHistoryRepository = Mockito.mock(MongoOrderStatusHistoryRepository.class);
    mongoOrderStatusHistoryService =
        Mockito.spy(new MongoOrderStatusHistoryServiceImpl(mongoOrderStatusHistoryRepository));
    grpcMongoOrderStatusHistoryService =
        new GrpcMongoOrderStatusHistoryServiceImpl(mongoOrderStatusHistoryService);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  public void whenInsertMongoOrderStatusHistories_shouldReturnListOfIdsOdStatusHistoriesSaved() {
    var grpcOrderStatusHistory1 = MongoOrderStatusHistory.newBuilder().build();
    var grpcOrderStatusHistory2 = MongoOrderStatusHistory.newBuilder().build();
    var orderStatusHistories = List.of(grpcOrderStatusHistory1, grpcOrderStatusHistory2);
    var orderStatusHistory1 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31cb"));
    orderStatusHistory1.setEtlStatus(EtlStatus.UNPROCESSED);
    var orderStatusHistory2 =
        MongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory(
            new ObjectId("63656f20f2a8a6a247ae31ca"));
    orderStatusHistory2.setEtlStatus(EtlStatus.UNPROCESSED);
    var orderStatusHistoriesSaved = List.of(orderStatusHistory1, orderStatusHistory2);
    var mongoOrderStatusHistoriesRequest =
        MongoOrderStatusHistoriesFromPythonRequest.newBuilder()
            .addAllMongoOrderStatusHistory(orderStatusHistories)
            .build();
    var insertMongoOrderStatusHistoriesResponse =
        InsertMongoOrderStatusHistoriesResponse.newBuilder()
            .addAllUuids(List.of("63656f20f2a8a6a247ae31cb", "63656f20f2a8a6a247ae31ca"))
            .build();
    var mockedStreamObserver =
        GrpcTestUtil.<InsertMongoOrderStatusHistoriesResponse>getMockedStreamObserver();
    Mockito.when(this.mongoOrderStatusHistoryRepository.saveAll(Mockito.any()))
        .thenReturn(orderStatusHistoriesSaved);
    var mockGrpcMongoOrderStatusHistoryMapper =
        Mockito.mockStatic(GrpcMongoOrderStatusHistoryMapper.class);
    this.grpcMongoOrderStatusHistoryService.insertMongoOrderStatusHistoriesFromPythonEtl(
        mongoOrderStatusHistoriesRequest, mockedStreamObserver);

    Mockito.verify(mockedStreamObserver, Mockito.times(1))
        .onNext(Mockito.eq(insertMongoOrderStatusHistoriesResponse));
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
    Mockito.verify(mongoOrderStatusHistoryService, Mockito.times(1))
        .insertMongoOrderStatusHistories(Mockito.any());
    Mockito.verify(mongoOrderStatusHistoryRepository, Mockito.times(1)).saveAll(Mockito.any());
  }
}
