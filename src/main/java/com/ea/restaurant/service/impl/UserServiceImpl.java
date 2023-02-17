package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.User;
import com.ea.restaurant.exceptions.EntityNotFoundException;
import com.ea.restaurant.repository.UserRepository;
import com.ea.restaurant.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public User create(User user) {
    user.setEntityStatus(Status.ACTIVE);
    user.setCreatedDate(Instant.now());
    user.setUpdatedBy(user.getCreatedBy());
    user.setUpdatedDate(user.getCreatedDate());
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<User> findAllEnabled() {
    return userRepository.findAllByEntityStatus(Status.ACTIVE);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> findEnabledById(Long id) {
    return userRepository.findByIdAndEntityStatus(id, Status.ACTIVE);
  }

  @Override
  public void deleteUserById(Long id, User user) {
    user.setEntityStatus(Status.DELETED);
    this.updateById(id, user);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public User updateById(Long id, User user) {
    User userToUpdate = this.findEnabledById(id).orElseThrow(EntityNotFoundException::new);
    userToUpdate.setUpdatedDate(Instant.now());
    userToUpdate.setUpdatedBy(user.getUpdatedBy());
    userToUpdate.setName(Optional.ofNullable(user.getName()).orElseGet(userToUpdate::getName));
    userToUpdate.setLastName(
        Optional.ofNullable(user.getLastName()).orElseGet(userToUpdate::getLastName));
    userToUpdate.setUsername(
        Optional.ofNullable(user.getUsername()).orElseGet(userToUpdate::getUsername));
    userToUpdate.setPassword(
        Optional.ofNullable(user.getPassword()).orElseGet(userToUpdate::getPassword));
    userToUpdate.setRoles(Optional.ofNullable(user.getRoles()).orElseGet(userToUpdate::getRoles));
    userToUpdate.setType(Optional.ofNullable(user.getType()).orElseGet(userToUpdate::getType));
    userToUpdate.setEntityStatus(
        Optional.ofNullable(user.getEntityStatus()).orElseGet(userToUpdate::getEntityStatus));
    userRepository.save(userToUpdate);
    return userToUpdate;
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> findEnabledByUsername(String username) {
    return userRepository.findByUsernameAndEntityStatus(username, Status.ACTIVE);
  }
}
