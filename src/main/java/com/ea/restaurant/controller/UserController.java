package com.ea.restaurant.controller;

import com.ea.restaurant.entities.User;
import com.ea.restaurant.service.impl.UserServiceImpl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserServiceImpl userService;

  public UserController(UserServiceImpl userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    var userSaved = userService.create(user);
    return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
    var userReturned = userService.findEnabledById(id);

    return userReturned
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    var users = userService.findAllEnabled();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/byUsername/{username}")
  public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
    var userReturned = userService.findEnabledByUsername(username);

    return userReturned
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<User> deleteUserById(@PathVariable("id") Long id, @RequestBody User user) {
    userService.deleteUserById(id, user);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUserById(@PathVariable("id") Long id, @RequestBody User user) {
    var userUpdated = userService.updateById(id, user);
    return new ResponseEntity<>(userUpdated, HttpStatus.OK);
  }
}
