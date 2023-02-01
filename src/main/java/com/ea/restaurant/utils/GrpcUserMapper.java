package com.ea.restaurant.utils;

import com.ea.restaurant.entities.User;
import com.ea.restaurant.grpc.GrpcUser;
import java.util.Objects;

public final class GrpcUserMapper {
  public static GrpcUser mapUserToGrpcUser(User user) {

    return GrpcUser.newBuilder()
        .setId(Objects.requireNonNull(user).getId())
        .setName(user.getName())
        .setLastName(user.getLastName())
        .setUsername(user.getUsername())
        .setPassword(user.getPassword())
        .setRoles(user.getRoles().toString())
        .setType(user.getType().toString())
        .setEntityStatus(user.getEntityStatus().toString())
        .setCreatedBy(user.getCreatedBy())
        .setUpdatedBy(user.getUpdatedBy())
        .build();
  }
}
