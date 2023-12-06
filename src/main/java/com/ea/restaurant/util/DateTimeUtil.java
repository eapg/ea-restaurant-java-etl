package com.ea.restaurant.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

  public static Instant applyTimezoneToInstant(Instant instant) {
    return applyTimezoneToInstant(instant, ZoneId.systemDefault());
  }

  public static Instant applyTimezoneToInstant(Instant instant, ZoneId zoneId) {
    if (instant == null) return null;
    var offset = ZonedDateTime.ofInstant(instant, zoneId).getOffset();
    var offSetHours = TimeUnit.SECONDS.toHours(offset.getTotalSeconds());

    return instant.plus(offSetHours, ChronoUnit.HOURS);
  }

  public static Instant getUtcFromInstant(Instant instant) {
    return getUtcFromInstant(instant, ZoneId.systemDefault());
  }

  public static Instant getUtcFromInstant(Instant instant, ZoneId zoneId) {
    if (instant == null) return null;
    var offset = ZonedDateTime.ofInstant(instant, zoneId).getOffset();
    var offSetHours = TimeUnit.SECONDS.toHours(offset.getTotalSeconds());

    return instant.plus(-offSetHours, ChronoUnit.HOURS);
  }

  public static Date getDateFromInstantWithTimeZone(Instant instant) {
    return Date.from(applyTimezoneToInstant(instant));
  }
}
