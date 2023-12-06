package com.ea.restaurant.util;

import com.ea.restaurant.test.fixture.Oauth2Fixture;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GrpcUtilTest {

  @Test
  public void whenAuthorization_shouldExtractBasicToken() {
    var authorization = "Basic VEVTVDAxOlRFU1QtQ0xJRU5ULVNFQ1JFVA";
    var expectedBasicToken = "VEVTVDAxOlRFU1QtQ0xJRU5ULVNFQ1JFVA";
    var basicToken = GrpcUtil.extractBasicToken(authorization);
    Assertions.assertEquals(basicToken, expectedBasicToken);
  }

  @Test
  public void whenBasicToken_shouldReturnTokenDecoded() {
    var basicToken = "VEVTVDAxOlRFU1QtQ0xJRU5ULVNFQ1JFVA";
    var result = GrpcUtil.getBasicAuthenticationDecoded(basicToken);
    Assertions.assertEquals(result.clientId(), Oauth2Fixture.TEST_CLIENT_ID);
    Assertions.assertEquals(result.clientSecret(), Oauth2Fixture.TEST_CLIENT_SECRET);
  }

  @Test
  public void getCredentialsFromBasicAuthToken() {
    var testMetadata =
        Oauth2Fixture.buildMetadataWithBasicToken(
            Oauth2Fixture.TEST_CLIENT_ID, Oauth2Fixture.TEST_CLIENT_SECRET);
    var credentials = GrpcUtil.getCredentialsFromBasicAuthToken(testMetadata);
    Assertions.assertNotNull(testMetadata);
    Assertions.assertEquals(Oauth2Fixture.TEST_CLIENT_ID, credentials.clientId());
    Assertions.assertEquals(Oauth2Fixture.TEST_CLIENT_SECRET, credentials.clientSecret());
  }
}
