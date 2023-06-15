package com.ea.restaurant.util;

import static com.ea.restaurant.test.fixture.GrpcMongoOrderStatusHistoryFixture.buildMongoOrderStatusHistory;
import static com.ea.restaurant.test.fixture.GrpcMongoOrderStatusHistoryFixture.buildMongoOrderStatusHistoryWithOutToStatusAndToTime;
import static com.ea.restaurant.util.GrpcMongoOrderStatusHistoryMapper.mapGrpcMongoOrderStatusToMongoOrderStatusHistory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GrpcMongoOrderStatusHistoryMapperTest {

  @Test
  void whenMapGrpcMongoOrderStatusHistory_shouldReturnMongoOrderStatusHistory() {
    var grpcMongoOrderStatusHistory = buildMongoOrderStatusHistory("63656f20f2a8a6a247ae31cb");

    var mappedGrpcMongoOrderStatusToMongoOrderStatusHistory =
        mapGrpcMongoOrderStatusToMongoOrderStatusHistory(grpcMongoOrderStatusHistory);

    Assertions.assertEquals(
        grpcMongoOrderStatusHistory.getFromStatus(),
        mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getFromStatus().toString());
    Assertions.assertEquals(
        grpcMongoOrderStatusHistory.getOrderId(),
        mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getOrderId());
    Assertions.assertEquals(
        grpcMongoOrderStatusHistory.getEtlStatus(),
        mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getEtlStatus().toString());
    Assertions.assertEquals(
        grpcMongoOrderStatusHistory.getEntityStatus(),
        mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getEntityStatus().toString());
    Assertions.assertEquals(
        grpcMongoOrderStatusHistory.getCreatedBy(),
        mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getCreatedBy());
  }

  @Test
  void whenMapGrpcMongoOrderStatusHistoryWithoutToStatusAndToTime_ShouldReturnNull() {
    var grpcMongoOrderStatusHistory =
        buildMongoOrderStatusHistoryWithOutToStatusAndToTime("63656f20f2a8a6a247ae31cb");

    var mappedGrpcMongoOrderStatusToMongoOrderStatusHistory =
        mapGrpcMongoOrderStatusToMongoOrderStatusHistory(grpcMongoOrderStatusHistory);

    Assertions.assertNull(mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getToStatus());
    Assertions.assertNull(mappedGrpcMongoOrderStatusToMongoOrderStatusHistory.getToTime());
  }
}
