package com.ea.restaurant.repository;

import com.ea.restaurant.entities.AppAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAccessTokenRepository extends JpaRepository<AppAccessToken, Long> {}
