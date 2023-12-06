package com.ea.restaurant.repository;

import com.ea.restaurant.entities.AppRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRefreshTokenRepository extends JpaRepository<AppRefreshToken, Long> {
  @Query(
      value =
          """
                      SELECT aprt.*
                        FROM app_refresh_tokens aprt
                       INNER JOIN app_access_tokens apat
                          ON aprt.id = apat.refresh_token_id
                       INNER JOIN app_clients apcl
                          ON apcl.id = aprt.app_client_id
                       WHERE aprt.token = :refresh_token
                         AND apat.token = :access_token
                         AND apcl.client_id = :client_id
                         AND apcl.entity_status = 'ACTIVE'
                      """,
      nativeQuery = true)
  Optional<AppRefreshToken> findByAccessTokenAndRefreshTokenAndClientId(
      @Param("access_token") String accessToken,
      @Param("refresh_token") String refreshToken,
      @Param("client_id") String clientId);
}
