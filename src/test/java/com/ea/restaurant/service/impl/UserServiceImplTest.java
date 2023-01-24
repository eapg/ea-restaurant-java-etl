package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.User;
import com.ea.restaurant.repository.UserRepository;
import com.ea.restaurant.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {

  private UserService userService;
  private UserRepository userRepository;

  @BeforeEach
  void setUp() throws Exception {

    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserServiceImpl(userRepository);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenUserCreate_ShouldCallRepositorySave() {
    var user = new User(2L);
    var userSaved = this.userService.create(user);
    Mockito.verify(this.userRepository, Mockito.times(1)).save(user);
  }

  @Test
  public void whenFindUserById_ShouldCallRepositoryFindById() {
    var user = Optional.of(new User(2L));
    Mockito.when(this.userRepository.findByIdAndEntityStatus(2L, Status.ACTIVE)).thenReturn(user);
    var userReturned = this.userService.findEnabledById(2L);
    Mockito.verify(userRepository, Mockito.times(1)).findByIdAndEntityStatus(2L, Status.ACTIVE);
    Assertions.assertEquals(user, userReturned);
  }

  @Test
  public void whenFindUserByUsername_ShouldCallRepositoryFindByUsername() {
    var user = Optional.of(new User(2L));
    user.get().setUsername("eap");
    Mockito.when(this.userRepository.findByUsernameAndEntityStatus("eap", Status.ACTIVE))
        .thenReturn(user);
    var userReturned = this.userService.findEnabledByUsername("eap");
    Mockito.verify(userRepository, Mockito.times(1))
        .findByUsernameAndEntityStatus("eap", Status.ACTIVE);
    Assertions.assertEquals(user, userReturned);
  }

  @Test
  public void whenFindAllUsers_ShouldCallRepositoryFindAll() {
    var users = new ArrayList<User>();
    var user1 = new User(1L);
    var user2 = new User(2L);
    users.add(user1);
    users.add(user2);
    Mockito.when(this.userRepository.findAllByEntityStatus(Status.ACTIVE)).thenReturn(users);
    var usersReturned = this.userService.findAllEnabled();
    Mockito.verify(userRepository, Mockito.times(1)).findAllByEntityStatus(Status.ACTIVE);
    Assertions.assertEquals(usersReturned, users);
  }

  @Test
  public void whenDeleteUserById_ShouldCallRepositoryDeleteById() {
    var userToDelete = new User(2L);
    var user = Optional.of(new User(2L));
    Mockito.when(this.userRepository.findByIdAndEntityStatus(2L, Status.ACTIVE)).thenReturn(user);
    this.userService.deleteUserById(2L, userToDelete);
    Mockito.verify(userRepository, Mockito.times(1)).findByIdAndEntityStatus(2L, Status.ACTIVE);
    Mockito.verify(userRepository, Mockito.times(1)).save(user.get());
  }

  @Test
  public void whenUpdateById_ShouldCallRepositoryUpdateById() {
    var userToUpdate = new User(2L);
    userToUpdate.setUsername("eapg");
    var user = Optional.of(new User(2L));
    user.get().setUsername("eap");
    Mockito.when(this.userRepository.findByIdAndEntityStatus(2L, Status.ACTIVE)).thenReturn(user);
    var userUpdated = this.userService.updateById(2L, userToUpdate);
    Mockito.verify(userRepository, Mockito.times(1)).findByIdAndEntityStatus(2L, Status.ACTIVE);
    Mockito.verify(userRepository, Mockito.times(1)).save(user.get());
    Assertions.assertEquals(userUpdated.getUsername(), "eapg");
  }
}
