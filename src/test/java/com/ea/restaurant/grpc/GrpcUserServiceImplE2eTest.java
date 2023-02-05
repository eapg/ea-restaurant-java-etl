package com.ea.restaurant.grpc;

import com.ea.restaurant.service.UserService;
import com.ea.restaurant.service.impl.utils.fixtures.UserFixture;
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
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
    properties = {
      "grpc.server.inProcessName=test",
      "grpc.server.port=-1",
      "grpc.client.inProcess.address=in-process:test"
    })
@ImportAutoConfiguration({
  GrpcServerAutoConfiguration.class,
  GrpcServerFactoryAutoConfiguration.class,
  GrpcClientAutoConfiguration.class
})
public class GrpcUserServiceImplE2eTest {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private UserService userService;

  @GrpcClient("inProcess")
  private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");
    jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART");
  }

  @Test
  void whenFindUserById_shouldReturnExpectedUser() {
    var userToTest = UserFixture.buildUser();
    var userSaved = this.userService.create(userToTest);
    FindUserByIdRequest findUserByIdRequest =
        FindUserByIdRequest.newBuilder().setId(userSaved.getId()).build();

    GrpcUser grpcUserReturned = userServiceBlockingStub.findUserById(findUserByIdRequest);
    Assertions.assertEquals(userToTest.getId(), grpcUserReturned.getId());
  }
}
