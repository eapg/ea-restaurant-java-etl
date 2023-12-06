package com.ea.restaurant.repository;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByIdAndEntityStatus(Long id, Status status);

  Optional<User> findByUsernameAndEntityStatus(String username, Status status);

  List<User> findAllByEntityStatus(Status status);
}
