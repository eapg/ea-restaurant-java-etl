package com.ea.restaurant.controller;

import com.ea.restaurant.entities.User;
import com.ea.restaurant.service.impl.utils.fixtures.UserFixture;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerE2ETest {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private TestRestTemplate restTemplate;

  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");
    jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART");

    user1 = UserFixture.buildUser();
    user2 = UserFixture.buildUser();
  }

  @Test
  void whenControllerCreate_shouldCreateUser() {

    var userSavedExpected = UserFixture.buildUser(1L);

    var request = new HttpEntity<>(this.user1);
    var createResponse = this.restTemplate.postForEntity("/users", request, User.class);
    Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    Assertions.assertEquals(
        userSavedExpected.getId(), Objects.requireNonNull(createResponse.getBody()).getId());
  }

  @Test
  void whenControllerGetById_ShouldReturnExpectedUser() {

    var request = new HttpEntity<>(this.user1);
    var createResponse = this.restTemplate.postForEntity("/users", request, User.class);
    var getByIdResponse =
        restTemplate.getForEntity(
            "/users/{id}",
            User.class,
            Map.of("id", Objects.requireNonNull(createResponse.getBody()).getId()));
    Assertions.assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
    Assertions.assertEquals(
        Objects.requireNonNull(createResponse.getBody()).getId(),
        Objects.requireNonNull(getByIdResponse.getBody()).getId());
  }

  @Test
  void whenControllerGetAll_ShouldReturnAllUsers() {

    var request1 = new HttpEntity<>(this.user1);
    var request2 = new HttpEntity<>(this.user2);
    var createResponse1 = this.restTemplate.postForEntity("/users", request1, User.class);
    var createResponse2 = this.restTemplate.postForEntity("/users", request2, User.class);
    var usersSaved = Arrays.asList(createResponse1.getBody(), createResponse2.getBody());
    var getAllResponse =
        restTemplate.exchange(
            "/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
    Assertions.assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
    Assertions.assertEquals(
        usersSaved.size(), Objects.requireNonNull(getAllResponse.getBody()).size());
  }

  @Test
  void whenControllerGetByUsername_ShouldReturnUser() {
    var request1 = new HttpEntity<>(this.user1);
    var createResponse1 = this.restTemplate.postForEntity("/users", request1, User.class);
    var getByUsernameResponse =
        restTemplate.getForEntity(
            "/users/byUsername/{username}",
            User.class,
            Map.of("username", Objects.requireNonNull(createResponse1.getBody()).getUsername()));
    Assertions.assertEquals(HttpStatus.OK, getByUsernameResponse.getStatusCode());
    Assertions.assertEquals(
        Objects.requireNonNull(request1.getBody()).getUsername(),
        Objects.requireNonNull(getByUsernameResponse.getBody()).getUsername());
  }

  @Test
  void whenControllerDeleteById_ShouldDeleteUser() {
    var userToDelete = new User();
    userToDelete.setUpdatedBy(5L);
    var request1 = new HttpEntity<>(this.user1);
    var request2 = new HttpEntity<>(userToDelete);

    var createResponse1 = this.restTemplate.postForEntity("/users", request1, User.class);
    var deleteResponse =
        this.restTemplate.exchange(
            "/users/{id}",
            HttpMethod.DELETE,
            request2,
            String.class,
            Map.of("id", Objects.requireNonNull(createResponse1.getBody()).getId()));
    var getByIdResponse =
        restTemplate.getForEntity(
            "/users/{id}",
            User.class,
            Map.of("id", Objects.requireNonNull(createResponse1.getBody()).getId()));
    Assertions.assertEquals(HttpStatus.NOT_FOUND, getByIdResponse.getStatusCode());
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
  }

  @Test
  void whenControllerUpdateById_ShouldUpdateUser() {
    var userToUpdate = new User();
    userToUpdate.setUpdatedBy(5L);
    userToUpdate.setUsername("new Username");

    var request1 = new HttpEntity<>(this.user1);
    var request2 = new HttpEntity<>(userToUpdate);
    var createResponse1 = this.restTemplate.postForEntity("/users", request1, User.class);
    var updateResponse =
        this.restTemplate.exchange(
            "/users/{id}",
            HttpMethod.PUT,
            request2,
            User.class,
            Map.of("id", Objects.requireNonNull(createResponse1.getBody()).getId()));
    Assertions.assertEquals(updateResponse.getStatusCode(), HttpStatus.OK);
    Assertions.assertEquals(
        userToUpdate.getUsername(), Objects.requireNonNull(updateResponse.getBody()).getUsername());
  }
}
