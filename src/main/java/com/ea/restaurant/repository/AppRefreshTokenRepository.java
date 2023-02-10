package com.ea.restaurant.repository;

import com.ea.restaurant.entities.AppRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRefreshTokenRepository extends JpaRepository<AppRefreshToken, Long> {}
