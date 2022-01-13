package com.example.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetails {

  private final String referenceNumber;
  private final String bsb;
  private final String account;
  private final double amount;

  public PaymentDetails(
      @JsonProperty(required = true, value = "referenceNumber") String referenceNumber,
      @JsonProperty(required = true, value = "bsb") String bsb,
      @JsonProperty(required = true, value = "account") String account,
      @JsonProperty(required = true, value = "amount") double amount) {
    this.referenceNumber = referenceNumber;
    this.bsb = bsb;
    this.account = account;
    this.amount = amount;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public String getBsb() {
    return bsb;
  }

  public String getAccount() {
    return account;
  }

  public double getAmount() {
    return amount;
  }
}
