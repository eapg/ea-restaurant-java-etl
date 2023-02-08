package com.ea.restaurant.repository;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppClientRepository extends JpaRepository<AppClient, Long> {
  Optional<AppClient> findByIdAndEntityStatus(Long id, Status status);

  Optional<AppClient> findByClientIdAndEntityStatus(String clientId, Status status);
}
