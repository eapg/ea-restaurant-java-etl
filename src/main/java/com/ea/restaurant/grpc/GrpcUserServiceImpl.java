package com.ea.restaurant.grpc;

import com.ea.restaurant.exceptions.EntityNotFoundException;
import com.ea.restaurant.grpc.UserServiceGrpc.UserServiceImplBase;
import com.ea.restaurant.service.UserService;
import com.ea.restaurant.util.GrpcUserMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcUserServiceImpl extends UserServiceImplBase {

  private final UserService userService;

  public GrpcUserServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void findUserById(FindUserByIdRequest request, StreamObserver<GrpcUser> responseObserver) {
    var user =
        this.userService.findEnabledById(request.getId()).orElseThrow(EntityNotFoundException::new);

    responseObserver.onNext(GrpcUserMapper.mapUserToGrpcUser(user));
    responseObserver.onCompleted();
  }
}
