package com.ea.restaurant.util;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.time.Instant;
import java.util.List;

public class GrpcMongoOrderStatusHistoryMapper {

  public static MongoOrderStatusHistory mapGrpcMongoOrderStatusToMongoOrderStatusHistory(
      com.ea.restaurant.grpc.MongoOrderStatusHistory mongoOrderStatusHistory) {
    return MongoOrderStatusHistory.builder()
        .orderId(mongoOrderStatusHistory.getOrderId())
        .fromStatus(OrderStatus.valueOf(mongoOrderStatusHistory.getFromStatus()))
        .toStatus(OrderStatus.valueOf(mongoOrderStatusHistory.getToStatus()))
        .fromTime(Instant.ofEpochMilli(mongoOrderStatusHistory.getFromTime()))
        .toTime(Instant.ofEpochMilli(mongoOrderStatusHistory.getToTime()))
        .etlStatus(EtlStatus.valueOf(mongoOrderStatusHistory.getEtlStatus()))
        .entityStatus(Status.valueOf(mongoOrderStatusHistory.getEntityStatus()))
        .createdBy(mongoOrderStatusHistory.getCreatedBy())
        .updatedBy(mongoOrderStatusHistory.getUpdatedBy())
        .createdDate(Instant.ofEpochMilli(mongoOrderStatusHistory.getCreatedDate()))
        .updatedDate(Instant.ofEpochMilli(mongoOrderStatusHistory.getUpdatedDate()))
        .build();
  }

  public static List<MongoOrderStatusHistory> mapGrpcMongoOrderStatusToMongoOrderStatusHistoryList(
      List<com.ea.restaurant.grpc.MongoOrderStatusHistory> grpcMongoOrderStatusHistories) {
    return grpcMongoOrderStatusHistories.stream()
        .map(GrpcMongoOrderStatusHistoryMapper::mapGrpcMongoOrderStatusToMongoOrderStatusHistory)
        .toList();
  }
}
