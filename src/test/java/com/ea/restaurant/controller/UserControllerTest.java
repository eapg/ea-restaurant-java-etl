package com.ea.restaurant.controller;

import static com.ea.restaurant.service.impl.utils.fixtures.UserFixture.buildUser;

import com.ea.restaurant.entities.User;
import com.ea.restaurant.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class UserControllerTest {

  private UserServiceImpl userService;
  private UserController userController;

  @BeforeEach
  void setUp() throws Exception {
    userService = Mockito.mock(UserServiceImpl.class);
    userController = new UserController(userService);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenControllerCreateUser_ShouldCallServiceCreate() {
    var user = buildUser();
    var userSaved = buildUser(1L);
    Mockito.when(this.userService.create(Mockito.eq(user))).thenReturn(userSaved);
    var userSavedResponse = this.userController.createUser(user);
    Mockito.verify(this.userService, Mockito.times(1)).create(Mockito.eq(user));
    Assertions.assertEquals(userSaved, userSavedResponse.getBody());
    Assertions.assertEquals(userSavedResponse.getStatusCode(), HttpStatus.CREATED);
  }

  @Test
  public void whenControllerGetById_ShouldCallServiceFindById() {
    var user = Optional.of(buildUser(2L));
    Mockito.when(this.userService.findEnabledById(Mockito.eq(2L))).thenReturn(user);
    var userReturned = this.userController.getUserById(2L);
    Mockito.verify(userService, Mockito.times(1)).findEnabledById(Mockito.eq(2L));
    System.out.println(userReturned.getBody());
    Assertions.assertEquals(user.get(), userReturned.getBody());

    Assertions.assertEquals(userReturned.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void whenControllerGetByUsername_ShouldCallServiceFindByUsername() {
    var user = Optional.of(buildUser(2L));
    user.get().setUsername("eap");
    Mockito.when(this.userService.findEnabledByUsername(Mockito.eq("eap"))).thenReturn(user);
    var userReturned = this.userController.getUserByUsername("eap");
    Mockito.verify(this.userService, Mockito.times(1)).findEnabledByUsername(Mockito.eq("eap"));
    Assertions.assertEquals(user.get(), userReturned.getBody());
    Assertions.assertEquals(userReturned.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void whenControllerGetAllUsers_ShouldCallServicesFindAll() {
    var users = new ArrayList<User>();
    var user1 = buildUser(1L);
    var user2 = buildUser(2L);
    users.add(user1);
    users.add(user2);
    Mockito.when(this.userService.findAllEnabled()).thenReturn(users);
    var usersReturned = this.userController.getAllUsers();
    Mockito.verify(this.userService, Mockito.times(1)).findAllEnabled();
    Assertions.assertEquals(usersReturned.getBody(), users);
    Assertions.assertEquals(usersReturned.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void whenControllerDeleteUser_ShouldCallServiceDeleteById() {
    var userToDelete = buildUser(2L);
    var deleteResponse = this.userController.deleteUserById(2L, userToDelete);
    Mockito.verify(this.userService, Mockito.times(1))
        .deleteUserById(Mockito.eq(2L), Mockito.eq(userToDelete));
    Assertions.assertEquals(deleteResponse.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void whenControllerUpdateUser_ShouldCallServiceUpdateById() {
    var userToUpdate = buildUser(2L);

    Mockito.when(this.userService.updateById(Mockito.eq(2L), Mockito.eq(userToUpdate)))
        .thenReturn(userToUpdate);
    var userUpdated = this.userController.updateUserById(2L, userToUpdate);
    Mockito.verify(this.userService, Mockito.times(1))
        .updateById(Mockito.eq(2L), Mockito.eq(userToUpdate));
    Assertions.assertEquals(userToUpdate, userUpdated.getBody());
    Assertions.assertEquals(userUpdated.getStatusCode(), HttpStatus.OK);
  }
}
