package com.ea.restaurant.service.impl;

import com.ea.restaurant.entities.User;
import com.ea.restaurant.exceptions.EntityNotFoundException;
import com.ea.restaurant.repository.UserRepository;
import com.ea.restaurant.service.UserService;
import com.ea.restaurant.service.impl.utils.fixtures.UserFixture;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class UserServiceE2ETest {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private UserRepository userRepository;

  @Autowired private UserService userService;

  private User userToTest1;
  private User userToTest2;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");
    jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART");
    userService = new UserServiceImpl(userRepository);
    userToTest1 = UserFixture.buildUser(1L);
    userToTest2 = UserFixture.buildUser(2L);
  }

  @Test
  public void whenCreate_ShouldCreateUser() {
    var userSaved = this.userService.create(this.userToTest1);
    var userReturned = this.userService.findEnabledById(userSaved.getId());
    var expectedUser = UserFixture.buildUser();
    Assertions.assertEquals(expectedUser, userSaved);
  }

  @Test
  public void whenFindById_ShouldReturnUserById() {
    var userSaved = this.userService.create(this.userToTest1);
    var userReturned = this.userService.findEnabledById(userSaved.getId());
    Assertions.assertEquals(userReturned, Optional.of(this.userToTest1));
  }

  @Test
  public void whenFindByUsername_ShouldReturnUserByUsername() {
    this.userService.create(this.userToTest2);
    var userReturned =
        this.userService
            .findEnabledByUsername(this.userToTest2.getUsername())
            .orElseThrow(EntityNotFoundException::new);
    Assertions.assertEquals(this.userToTest2.getUsername(), userReturned.getUsername());
  }

  @Test
  public void whenFindAll_ShouldReturnAllEnabledUsers() {
    this.userService.create(this.userToTest1);
    this.userService.create(this.userToTest2);
    var users = Arrays.asList(this.userToTest1, this.userToTest2);
    var usersReturned = this.userService.findAllEnabled();
    Assertions.assertEquals(usersReturned, users);
  }

  @Test
  public void whenDelete_ShouldDeleteUser() {
    var user1 = this.userService.create(this.userToTest1);
    var user2 = this.userService.create(this.userToTest2);
    var userToDelete =
        this.userService.findEnabledById(user1.getId()).orElseThrow(EntityNotFoundException::new);
    this.userService.deleteUserById(userToDelete.getId(), userToDelete);
    var listUserAfterDeleteUser1 = Arrays.asList(this.userToTest2);
    var usersReturned = this.userService.findAllEnabled();
    Assertions.assertEquals(listUserAfterDeleteUser1, usersReturned);
  }

  @Test
  public void whenUpdate_ShouldUpdateUser() {
    var user1 = this.userService.create(this.userToTest1);
    user1.setUsername("new user name");
    var expectedUser = UserFixture.buildUser(1L, "ea", "pg", "new user name", "1234");

    var userUpdated = this.userService.updateById(user1.getId(), user1);
    Assertions.assertEquals(userUpdated.getUsername(), expectedUser.getUsername());
  }
}
