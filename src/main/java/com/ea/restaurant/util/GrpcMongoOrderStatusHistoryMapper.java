package com.ea.restaurant.util;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class GrpcMongoOrderStatusHistoryMapper {

  public static MongoOrderStatusHistory mapGrpcMongoOrderStatusToMongoOrderStatusHistory(
      com.ea.restaurant.grpc.MongoOrderStatusHistory mongoOrderStatusHistory) {
    return MongoOrderStatusHistory.builder()
        .orderId(mongoOrderStatusHistory.getOrderId())
        .fromStatus(OrderStatus.valueOf(mongoOrderStatusHistory.getFromStatus()))
        .toStatus(
            Optional.ofNullable(mongoOrderStatusHistory)
                .filter(com.ea.restaurant.grpc.MongoOrderStatusHistory::hasToStatus)
                .map(mongoOrder -> mongoOrder.getToStatus().getValue())
                .map(OrderStatus::valueOf)
                .orElse(null))
        .fromTime(Instant.ofEpochMilli(mongoOrderStatusHistory.getFromTime()))
        .toTime(
            Optional.ofNullable(mongoOrderStatusHistory)
                .filter(com.ea.restaurant.grpc.MongoOrderStatusHistory::hasToStatus)
                .map(mongoOrder -> mongoOrder.getToTime().getValue())
                .map(Instant::ofEpochMilli)
                .orElse(null))
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
