package com.ea.restaurant.utils;

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
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.text.ParseException;
import java.util.Date;

public class Oauth2Util {

  public static String buildClientCredentialsToken(
      AppClient client,
      AppClientScope appClientScope,
      String oauth2Secret,
      Oauth2.TokenType tokenType)
      throws JOSEException {
    Date now = new Date();
    var expirationTime =
        (tokenType == Oauth2.TokenType.ACCESS_TOKEN)
            ? (client.getAccessTokenExpirationTime())
            : (client.getRefreshTokenExpirationTime());

    var claims =
        new JWTClaimsSet.Builder()
            .claim("clientName", client.getClientName())
            .claim("scopes", appClientScope.getScopes())
            .expirationTime(new Date(now.getTime() + expirationTime))
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
    ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor =
        new DefaultJWTProcessor<SimpleSecurityContext>();
    JWKSource<SimpleSecurityContext> jweKeySource =
        new ImmutableSecret<SimpleSecurityContext>(secretKey.getBytes());
    JWEKeySelector<SimpleSecurityContext> jweKeySelector =
        new JWEDecryptionKeySelector<SimpleSecurityContext>(
            JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
    jwtProcessor.setJWEKeySelector(jweKeySelector);

    return jwtProcessor.process(token, null);
  }
}
