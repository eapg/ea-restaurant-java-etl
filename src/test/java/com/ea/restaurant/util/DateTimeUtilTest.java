package com.ea.restaurant.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

  @BeforeEach
  void setUp() {}

  @Test
  void whenApplyTimezoneToInstant_shouldReturnAndInstantWithTimeZoneApplied() {

    var stringDateTime = "2020-10-09T12:00:00.00Z";
    var instantDateTime = Instant.parse(stringDateTime);
    var instantDateTimeWithAppliedTimeZone =
        DateTimeUtil.applyTimezoneToInstant(instantDateTime, ZoneId.systemDefault());
    var utcDateTime =
        DateTimeUtil.getUtcFromInstant(instantDateTimeWithAppliedTimeZone, ZoneId.systemDefault());
    Assertions.assertEquals(instantDateTime, utcDateTime);
  }
}
