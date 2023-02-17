package com.ea.restaurant.grpc;

import com.ea.restaurant.service.UserService;
import com.ea.restaurant.test.fixture.UserFixture;
import com.ea.restaurant.test.util.GrpcTestUtil;
import com.ea.restaurant.util.GrpcUserMapper;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GrpcUserServiceImplTest {

  private UserService userService;
  private GrpcUserServiceImpl grpcUserService;
  private AutoCloseable mockedAnnotations;

  @BeforeEach
  void setUp() {
    userService = Mockito.mock(UserService.class);
    grpcUserService = new GrpcUserServiceImpl(userService);
    this.mockedAnnotations = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void afterEach() throws Exception {
    this.mockedAnnotations.close();
  }

  @Test
  void whenFindUserById_shouldReturnExpectedUser() {
    var finUserByIdRequest = FindUserByIdRequest.newBuilder().setId(2L).build();
    var user = UserFixture.buildUser(2L);
    var mockedStreamObserver = GrpcTestUtil.<GrpcUser>getMockedStreamObserver();
    Mockito.when(this.userService.findEnabledById(Mockito.eq(user.getId())))
        .thenReturn(Optional.of(user));
    grpcUserService.findUserById(finUserByIdRequest, mockedStreamObserver);
    Mockito.verify(userService, Mockito.times(1)).findEnabledById(Mockito.eq(2L));
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onCompleted();
    var expectedGrpcUser = GrpcUserMapper.mapUserToGrpcUser(user);
    Mockito.verify(mockedStreamObserver, Mockito.times(1)).onNext(Mockito.eq(expectedGrpcUser));
  }
}
