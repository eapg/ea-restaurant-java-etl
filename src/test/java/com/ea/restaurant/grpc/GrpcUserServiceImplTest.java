package com.ea.restaurant.grpc;

import com.ea.restaurant.service.UserService;
import com.ea.restaurant.service.impl.utils.fixtures.UserFixture;
import com.ea.restaurant.utils.GrpcUserMapper;
import com.ea.restaurant.utils.GrpcUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GrpcUserServiceImplTest {

  private UserService userService;
  private GrpcUserServiceImpl grpcUserService;

  @BeforeEach
  void setUp() {
    userService = Mockito.mock(UserService.class);
    grpcUserService = new GrpcUserServiceImpl(userService);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void whenFindUserById_shouldReturnExpectedUser() {
    var finUserByIdRequest = FindUserByIdRequest.newBuilder().setId(2L).build();
    var user = UserFixture.buildUser(2L);
    var mockedStreamObserver = GrpcUtils.<GrpcUser>getMockedStreamObserver();
    Mockito.when(this.userService.findEnabledById(Mockito.eq(user.getId())))
        .thenReturn(Optional.of(user));
    grpcUserService.findUserById(finUserByIdRequest, mockedStreamObserver);
    Mockito.verify(userService, Mockito.times(1)).findEnabledById(Mockito.eq(2L));
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
    var expectedGrpcUser = GrpcUserMapper.mapUserToGrpcUser(user);
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onNext(Mockito.eq(expectedGrpcUser));
  }
}
