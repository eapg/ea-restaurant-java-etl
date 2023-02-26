package com.ea.restaurant.grpc;

import com.ea.restaurant.constants.Status;
import com.ea.restaurant.exceptions.EntityNotFoundException;
import com.ea.restaurant.repository.AppClientRepository;
import com.ea.restaurant.repository.AppClientScopeRepository;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.test.fixture.Oauth2Fixture;
import com.ea.restaurant.test.util.AuthenticationCallCredentials;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@ImportAutoConfiguration({
  GrpcServerAutoConfiguration.class,
  GrpcServerFactoryAutoConfiguration.class,
  GrpcClientAutoConfiguration.class
})
public class GrpcOauth2ServiceImplE2eTest {

  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private Oauth2Service oauth2Service;
  @Autowired private AppClientRepository appClientRepository;
  @Autowired private AppClientScopeRepository appClientScopeRepository;

  @GrpcClient("inProcess")
  private Oauth2ServiceGrpc.Oauth2ServiceBlockingStub oauth2ServiceBlockingStub;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("TRUNCATE TABLE app_access_tokens CASCADE");
    jdbcTemplate.execute("ALTER SEQUENCE app_access_tokens_id_seq RESTART");
    jdbcTemplate.execute("TRUNCATE TABLE app_refresh_tokens CASCADE");
    jdbcTemplate.execute("ALTER SEQUENCE app_refresh_tokens_id_seq RESTART");
  }

  @Test
  public void whenLoginClientCredentials_ShouldReturnGrpcLoginResponse() {
    var metadata = Oauth2Fixture.buildMetadataWithBasicToken("postman001", "postmansecret01");
    var client =
        appClientRepository
            .findByIdAndEntityStatus(1L, Status.ACTIVE)
            .orElseThrow(EntityNotFoundException::new);
    var scopes =
        appClientScopeRepository
            .findByAppClientIdAndEntityStatus(client.getId(), Status.ACTIVE)
            .orElseThrow(EntityNotFoundException::new);

    var grpcLoginResponse =
        oauth2ServiceBlockingStub
            .withCallCredentials(new AuthenticationCallCredentials(metadata))
            .loginClient(null);

    Assertions.assertEquals(grpcLoginResponse.getClientName(), client.getClientName());
    Assertions.assertEquals(grpcLoginResponse.getScopes(), scopes.getScopes());
    Assertions.assertEquals(
        grpcLoginResponse.getExpiresIn(), client.getAccessTokenExpirationTime());
  }
}
