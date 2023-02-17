package com.ea.restaurant.util;

import com.ea.restaurant.constants.Oauth2;
import com.ea.restaurant.entities.AppClient;
import com.ea.restaurant.entities.AppClientScope;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

public class Oauth2Util {

  public static String buildClientCredentialsToken(
      AppClient client,
      AppClientScope appClientScope,
      String oauth2Secret,
      Oauth2.TokenType tokenType)
      throws JOSEException {
    var nowInstant = Instant.now();
    var expirationTime =
        (tokenType == Oauth2.TokenType.ACCESS_TOKEN)
            ? (client.getAccessTokenExpirationTime())
            : (client.getRefreshTokenExpirationTime());

    var claims =
        new JWTClaimsSet.Builder()
            .issueTime(Date.from(nowInstant))
            .issuer(Oauth2.Claims.JAVA_ETL.toString())
            .subject(client.getClientName())
            .claim("clientName", client.getClientName())
            .claim("scopes", appClientScope.getScopes())
            .expirationTime(Date.from(nowInstant.plus(expirationTime, ChronoUnit.SECONDS)))
            .build();

    var payload = new Payload(claims.toJSONObject());

    var header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

    var secretKeyEncrypted = oauth2Secret.getBytes();
    var encrypted = new DirectEncrypter(secretKeyEncrypted);
    var token = new JWEObject(header, payload);
    token.encrypt(encrypted);
    return token.serialize();
  }

  public static JWTClaimsSet getTokenDecoded(String token, String secretKey)
      throws BadJOSEException, ParseException, JOSEException {
    var jwtProcessor = buildJwtProcessor(secretKey);

    return jwtProcessor.process(token, null);
  }

  public static void validateToken(String token, String secretKey)
      throws BadJOSEException, ParseException, JOSEException {
    getTokenDecoded(token, secretKey);
  }

  public static Integer getExpirationTimeInSeconds(Instant expirationTime) {
    var now = Instant.now();
    return Math.toIntExact((expirationTime.getEpochSecond() - now.getEpochSecond()));
  }

  private static ConfigurableJWTProcessor<SimpleSecurityContext> buildJwtProcessor(
      String secretKey) {
    var jwtProcessor = new DefaultJWTProcessor<SimpleSecurityContext>();
    var jweKeySource = new ImmutableSecret<SimpleSecurityContext>(secretKey.getBytes());
    var jweKeySelector =
        new JWEDecryptionKeySelector<>(
            JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
    jwtProcessor.setJWEKeySelector(jweKeySelector);

    jwtProcessor.setJWTClaimsSetVerifier(
        new DefaultJWTClaimsVerifier<>(new JWTClaimsSet.Builder().build(), Set.of("exp", "iat")));

    return jwtProcessor;
  }
}
