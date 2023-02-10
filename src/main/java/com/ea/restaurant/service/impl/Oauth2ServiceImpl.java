package com.ea.restaurant.service.impl;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.constants.Oauth2.GranType;
import com.ea.restaurant.constants.Status;
import com.ea.restaurant.dtos.LoginResponseDto;
import com.ea.restaurant.entities.AppAccessToken;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppRefreshToken;
import com.ea.restaurant.exceptions.BcryptException;
import com.ea.restaurant.exceptions.EntityNotFoundException;
import com.ea.restaurant.exceptions.WrongCredentialsException;
import com.ea.restaurant.repository.AppAccessTokenRepository;
import com.ea.restaurant.repository.AppClientRepository;
import com.ea.restaurant.repository.AppClientScopeRepository;
import com.ea.restaurant.repository.AppRefreshTokenRepository;
import com.ea.restaurant.service.Oauth2Service;
import com.ea.restaurant.utils.Oauth2Util;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {

  private final String oauth2SecretKey;
  private final AppClientRepository appClientRepository;
  private final AppClientScopeRepository appClientScopeRepository;
  private final AppRefreshTokenRepository appRefreshTokenRepository;
  private final AppAccessTokenRepository appAccessTokenRepository;

  public Oauth2ServiceImpl(
      @Value("${oauth2.secret.key}") String oauth2SecretKey,
      AppClientRepository appClientRepository,
      AppClientScopeRepository appClientScopeRepository,
      AppAccessTokenRepository appAccessTokenRepository,
      AppRefreshTokenRepository appRefreshTokenRepository) {
    this.oauth2SecretKey = oauth2SecretKey;
    this.appClientRepository = appClientRepository;
    this.appClientScopeRepository = appClientScopeRepository;
    this.appAccessTokenRepository = appAccessTokenRepository;
    this.appRefreshTokenRepository = appRefreshTokenRepository;
  }

  @Override
  public LoginResponseDto loginClient(String clientId, String clientSecret) throws JOSEException {
    validateClientCredentials(clientId, clientSecret);
    var client =
        this.appClientRepository
            .findByClientIdAndEntityStatus(clientId, Status.ACTIVE)
            .orElseThrow(WrongCredentialsException::new);
    var scopes =
        this.appClientScopeRepository
            .findByAppClientIdAndEntityStatus(client.getId(), Status.ACTIVE)
            .orElseThrow(EntityNotFoundException::new);
    var accessToken =
        Oauth2Util.buildClientCredentialsToken(
            client, scopes, oauth2SecretKey, Oauth2.TokenType.ACCESS_TOKEN);
    var refreshToken =
        Oauth2Util.buildClientCredentialsToken(
            client, scopes, oauth2SecretKey, Oauth2.TokenType.REFRESH_TOKEN);

    var persistedRefreshToken = createRefreshToken(refreshToken, client);
    createAccessToken(accessToken, persistedRefreshToken);

    return LoginResponseDto.builder()
        .clientName(client.getClientName())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .scopes(scopes.getScopes())
        .expiresIn(client.getAccessTokenExpirationTime())
        .build();
  }

  private void createAccessToken(String accessToken, AppRefreshToken appRefreshToken) {
    var appAccessToken = new AppAccessToken();
    appAccessToken.setRefreshTokenId(appRefreshToken.getId());
    appAccessToken.setToken(accessToken);
    this.appAccessTokenRepository.save(appAccessToken);
  }

  private AppRefreshToken createRefreshToken(String refreshToken, AppClient client) {
    var appRefreshToken = new AppRefreshToken();
    appRefreshToken.setToken(refreshToken);
    appRefreshToken.setAppClientId(client.getId());
    appRefreshToken.setGrantType(GranType.CLIENT_CREDENTIALS);
    return this.appRefreshTokenRepository.save(appRefreshToken);
  }

  private void validateClientCredentials(String clientId, String clientSecret) {

    var client =
        this.appClientRepository
            .findByClientIdAndEntityStatus(clientId, Status.ACTIVE)
            .orElseThrow(WrongCredentialsException::new);

    if (!(BCrypt.checkpw(clientSecret, client.getClientSecret()))) {
      throw new BcryptException();
    }
  }
}
