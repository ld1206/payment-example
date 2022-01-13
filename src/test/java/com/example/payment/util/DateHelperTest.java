package com.example.payment.util;

import org.junit.jupiter.api.Test;

import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateHelperTest {
  @Test
  public void isWorkHour_Ture() {
    assertTrue(DateHelper.isWorkHour(new GregorianCalendar(2022, 0, 13, 10, 15).getTime()));
  }

  @Test
  public void isWorkHour_False() {
    assertFalse(DateHelper.isWorkHour(new GregorianCalendar(2022, 0, 15, 10, 15).getTime()));
    assertFalse(DateHelper.isWorkHour(new GregorianCalendar(2022, 0, 13, 5, 15).getTime()));
    assertFalse(DateHelper.isWorkHour(new GregorianCalendar(2022, 0, 14, 17, 01).getTime()));
  }
}
