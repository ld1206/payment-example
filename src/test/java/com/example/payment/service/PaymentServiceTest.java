package com.example.payment.service;

import com.example.payment.exception.InvalidAmountException;
import com.example.payment.exception.InvalidDateException;
import com.example.payment.model.PaymentDetails;
import com.example.payment.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentServiceTest {

  @Autowired private PaymentService paymentService;

  @Test
  public void writeToFile_Success() throws IOException {
    File file = File.createTempFile("test", ".csv");

    final var date = new GregorianCalendar(2022, 0, 13, 10, 15).getTime();
    final var referenceNumber = RandomStringUtils.randomNumeric(6);
    final var bsb = RandomStringUtils.randomNumeric(6);
    final var account = RandomStringUtils.randomNumeric(6);
    final var amount = 10;
    final var paymentDetails = new PaymentDetails(referenceNumber, bsb, account, amount);

    paymentService.writeToFile(paymentDetails, file, date);

    final var csvParser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT);
    final var csvRecord = csvParser.getRecords().get(0);
    assertEquals(date.toString(), csvRecord.get(0));
    assertEquals(referenceNumber, csvRecord.get(1));
    assertEquals(bsb, csvRecord.get(2));
    assertEquals(account, csvRecord.get(3));
    assertEquals(amount, Double.parseDouble(csvRecord.get(4)));
    assertEquals(0.91, Double.parseDouble(csvRecord.get(5)));
    assertEquals(9.09, Double.parseDouble(csvRecord.get(6)));

    file.deleteOnExit();
  }

  @Test
  void saveToCsv_Throw_InvalidDateException() {
    try (final var dateHelper = mockStatic(DateHelper.class)) {
      dateHelper.when(() -> DateHelper.isWorkHour(any())).thenReturn(false);
      assertThrows(
          InvalidDateException.class,
          () -> paymentService.saveToCsv(createSamplepaymentDetails(0.001)));
    }
  }

  @Test
  void saveToCsv_Throw_InvalidAmountException() {
    try (final var dateHelper = mockStatic(DateHelper.class)) {
      dateHelper.when(() -> DateHelper.isWorkHour(any())).thenReturn(true);
      assertThrows(
          InvalidAmountException.class,
          () -> paymentService.saveToCsv(createSamplepaymentDetails(0.001)));
    }
  }

  private PaymentDetails createSamplepaymentDetails(final double amount) {
    return new PaymentDetails(
        RandomStringUtils.randomNumeric(6),
        RandomStringUtils.randomNumeric(6),
        RandomStringUtils.randomNumeric(6),
        amount);
  }
}
