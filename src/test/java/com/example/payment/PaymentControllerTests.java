/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.payment;

import com.example.payment.service.PaymentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTests {

  private MockMvc mockMvc;

  @Mock PaymentService mockPaymentService;

  @InjectMocks PaymentController mockPaymentController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    this.mockMvc = MockMvcBuilders.standaloneSetup(mockPaymentController).build();
  }

  @Test
  public void paramPaymentShouldReturnPaymentDetails() throws Exception {

    final var referenceNumber = RandomStringUtils.randomNumeric(6);
    final var bsb = RandomStringUtils.randomNumeric(6);
    final var account = RandomStringUtils.randomNumeric(6);
    final var amount = 10;
    doNothing().when(mockPaymentService).saveToCsv(any());
    this.mockMvc
        .perform(
            post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new JSONObject()
                        .put("referenceNumber", referenceNumber)
                        .put("bsb", bsb)
                        .put("account", account)
                        .put("amount", amount)
                        .toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.referenceNumber").value(referenceNumber))
        .andExpect(jsonPath("$.bsb").value(bsb))
        .andExpect(jsonPath("$.account").value(account))
        .andExpect(jsonPath("$.amount").value(amount));
  }

  @Test
  public void noParamPaymentShouldThrowException() throws Exception {

    final var referenceNumber = RandomStringUtils.randomNumeric(6);
    final var bsb = RandomStringUtils.randomNumeric(6);
    final var account = RandomStringUtils.randomNumeric(6);
    final var amount = 10;
    doNothing().when(mockPaymentService).saveToCsv(any());
    this.mockMvc
        .perform(post("/pay").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
