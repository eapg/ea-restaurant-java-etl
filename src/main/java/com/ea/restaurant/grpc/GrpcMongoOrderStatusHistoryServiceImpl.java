package com.ea.restaurant.grpc;

import com.ea.restaurant.grpc.MongoOrderStatusHistoryServiceGrpc.MongoOrderStatusHistoryServiceImplBase;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.ea.restaurant.util.GrpcMongoOrderStatusHistoryMapper;
import com.ea.restaurant.util.GrpcMongoOrderStatusHistoryUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcMongoOrderStatusHistoryServiceImpl extends MongoOrderStatusHistoryServiceImplBase {

  private final MongoOrderStatusHistoryService mongoOrderStatusHistoryService;

  public GrpcMongoOrderStatusHistoryServiceImpl(
      MongoOrderStatusHistoryService mongoOrderStatusHistoryService) {
    this.mongoOrderStatusHistoryService = mongoOrderStatusHistoryService;
  }

  @Override
  public void insertMongoOrderStatusHistoriesFromPythonEtl(
      MongoOrderStatusHistoriesFromPythonRequest request,
      StreamObserver<InsertMongoOrderStatusHistoriesResponse> responseObserver) {

      var mappedMongoOrderStatusHistories =
          GrpcMongoOrderStatusHistoryMapper.mapGrpcMongoOrderStatusToMongoOrderStatusHistoryList(
              request.getMongoOrderStatusHistoryList());

      var savedMongoOrderStatusHistories =
          this.mongoOrderStatusHistoryService.insertMongoOrderStatusHistories(
              mappedMongoOrderStatusHistories);
      var mongoOrderStatusHistoriesUuids =
          GrpcMongoOrderStatusHistoryUtil.getMongoOrderStatusUuids(savedMongoOrderStatusHistories);

      responseObserver.onNext(
          InsertMongoOrderStatusHistoriesResponse.newBuilder()
              .addAllUuids(mongoOrderStatusHistoriesUuids)
              .build());

    responseObserver.onCompleted();
  }
}
