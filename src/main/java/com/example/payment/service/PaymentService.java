package com.example.payment.service;

import com.example.payment.exception.InvalidAmountException;
import com.example.payment.exception.InvalidDateException;
import com.example.payment.model.PaymentDetails;
import com.example.payment.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import static java.util.UUID.randomUUID;

@Service
public class PaymentService {

  private static final DecimalFormat df = new DecimalFormat("0.00");

  public void saveToCsv(@NonNull PaymentDetails paymentDetails) throws IOException {
    final var now = new Date();

    if (!DateHelper.isWorkHour(now)) throw new InvalidDateException();

    final var amount = paymentDetails.getAmount();
    if (amount < 0.01 || amount > 2000) {
      throw new InvalidAmountException();
    }

    File file = new File("payment-" + now.getTime() + "-" + randomUUID() + ".csv");
    if (!file.exists()) {
      file.createNewFile();
    }
    writeToFile(paymentDetails, file, now);
  }

  public void writeToFile(
      @NonNull PaymentDetails paymentDetails, @NonNull File file, @NonNull Date date)
      throws IOException {
    final var amount = paymentDetails.getAmount();
    final var gst = df.format(amount / 11);
    FileWriter out = new FileWriter(file, true);
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
      printer.printRecord(
          date.toString(),
          paymentDetails.getReferenceNumber(),
          paymentDetails.getBsb(),
          paymentDetails.getAccount(),
          amount,
          gst,
          amount - Double.parseDouble(gst));
      printer.flush();
    }
  }
}
