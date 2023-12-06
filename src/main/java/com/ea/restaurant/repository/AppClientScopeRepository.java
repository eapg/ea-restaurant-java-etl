package com.ea.restaurant.repository;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.entities.AppClientScope;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppClientScopeRepository extends JpaRepository<AppClientScope, Long> {
  Optional<AppClientScope> findByAppClientIdAndEntityStatus(Long appClientId, Status status);
}
