package com.example.payment.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.springframework.lang.NonNull;

public class DateHelper {
  public static boolean isWorkHour(@NonNull final Date date) {
    Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("Australia/Sydney"));
    cal.setTime(date);
    final var weekday = cal.get(Calendar.DAY_OF_WEEK);
    final var hour = cal.get(Calendar.HOUR_OF_DAY);
    return weekday < 5 && (hour >= 9 && hour < 17) || (weekday == 5 && (hour >= 8 && hour < 18));
  }
}
