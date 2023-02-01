package com.ea.restaurant.grpc;

import com.ea.restaurant.repository.UserRepository;
import com.ea.restaurant.service.impl.UserServiceImpl;
import com.ea.restaurant.service.impl.utils.fixtures.UserFixture;
import com.ea.restaurant.utils.GrpcUserMapper;
import com.ea.restaurant.utils.GrpcUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GrpcUserServiceImplIntegrationTest {

  private UserRepository userRepository;
  private UserServiceImpl userService;
  private GrpcUserServiceImpl grpcUserService;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userService = Mockito.spy(new UserServiceImpl(userRepository));
    grpcUserService = new GrpcUserServiceImpl(userService);
  }

  @Test
  void whenFindUserById_shouldReturnExpectedUserThroughGrpc() {
    var userToTest = UserFixture.buildUser(2L);
    var findUserByIdRequest = FindUserByIdRequest.newBuilder().setId(userToTest.getId()).build();
    var mockedStreamObserver = GrpcUtils.<GrpcUser>getMockedStreamObserver();
    var expectedGrpcUser = GrpcUserMapper.mapUserToGrpcUser(userToTest);
    Mockito.when(
            this.userRepository.findByIdAndEntityStatus(
                Mockito.eq(userToTest.getId()), Mockito.eq(userToTest.getEntityStatus())))
        .thenReturn(Optional.of(userToTest));
    this.grpcUserService.findUserById(findUserByIdRequest, mockedStreamObserver);
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onNext(Mockito.eq(expectedGrpcUser));
    Mockito.verify(this.userService, Mockito.times(1))
        .findEnabledById(Mockito.eq(userToTest.getId()));
  }
}
