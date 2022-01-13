package com.example.payment;

import com.example.payment.exception.InvalidAmountException;
import com.example.payment.exception.InvalidDateException;
import com.example.payment.model.PaymentDetails;
import com.example.payment.service.PaymentService;
import java.io.IOException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PaymentController {

  @Autowired private PaymentService paymentService;

  @PostMapping("/pay")
  public PaymentDetails pay(@NonNull @RequestBody PaymentDetails paymentDetails)
      throws IOException {
    paymentService.saveToCsv(paymentDetails);
    return paymentDetails;
  }

  @ExceptionHandler({InvalidDateException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleInvalidDateException() {
    return new JSONObject()
        .put(
            "error",
            "Payments are only allowed between 9am and 5pm, Monday to Thursday, or between 8am"
                + " and 6pm on Fridays.")
        .toString();
  }

  @ExceptionHandler({InvalidAmountException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleInvalidAmountException() {
    return new JSONObject()
        .put("error", "Payment amounts below $0.01 or above $2000.00 are not allowed.")
        .toString();
  }
}
