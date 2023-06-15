package com.ea.restaurant.grpc;

import com.ea.restaurant.constants.EtlStatus;
import com.ea.restaurant.constants.OrderStatus;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.document.MongoOrderStatusHistory;
import com.ea.restaurant.repository.MongoOrderStatusHistoryRepository;
import com.ea.restaurant.service.MongoOrderStatusHistoryService;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import java.util.List;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
@ImportAutoConfiguration({
  GrpcServerAutoConfiguration.class,
  GrpcServerFactoryAutoConfiguration.class,
  GrpcClientAutoConfiguration.class
})
public class GrpcMongoOrderStatusHistoryServiceImplE2eTest {
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private MongoOrderStatusHistoryService mongoOrderStatusHistoryService;
  @Autowired private MongoOrderStatusHistoryRepository mongoOrderStatusHistoryRepository;

  @GrpcClient("inProcess")
  private MongoOrderStatusHistoryServiceGrpc.MongoOrderStatusHistoryServiceBlockingStub
      mongoOrderStatusHistoryServiceBlockingStub;

  @BeforeEach
  public void setUp() {
    mongoTemplate.dropCollection(MongoOrderStatusHistory.class);
    mongoTemplate.createCollection(MongoOrderStatusHistory.class);
  }

  @Test
  public void whenInsertMongoOrderStatusHistoriesFromPythonEtl_shouldReturnUuidsSaved() {
    var grpcOrderStatusHistory1 =
        com.ea.restaurant.grpc.MongoOrderStatusHistory.newBuilder()
            .setOrderId(1)
            .setFromStatus(OrderStatus.NEW_ORDER.toString())
            .setToStatus(StringValue.of(OrderStatus.IN_PROCESS.toString()))
            .setFromTime(100)
            .setToTime(Int64Value.of(100))
            .setEtlStatus(EtlStatus.UNPROCESSED.toString())
            .setEntityStatus(Status.ACTIVE.toString())
            .setCreatedBy(1L)
            .setUpdatedBy(1L)
            .setCreatedDate(100)
            .setUpdatedDate(100)
            .build();

    var mongoOrderStatusHistories = List.of(grpcOrderStatusHistory1);
    var mongoOrderStatusHistoriesFromPythonRequest =
        MongoOrderStatusHistoriesFromPythonRequest.newBuilder()
            .addAllMongoOrderStatusHistory(mongoOrderStatusHistories)
            .build();

    var uuidsOfMongoOrderStatusHistoriesSaved =
        mongoOrderStatusHistoryServiceBlockingStub.insertMongoOrderStatusHistoriesFromPythonEtl(
            mongoOrderStatusHistoriesFromPythonRequest);
    Assertions.assertNotNull(uuidsOfMongoOrderStatusHistoriesSaved);
  }
}
