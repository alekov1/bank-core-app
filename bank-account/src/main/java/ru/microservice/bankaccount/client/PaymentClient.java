package ru.microservice.bankaccount.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ru.microservice.bankpayment.web.dto.PaymentResponse;

import java.util.List;

@FeignClient(name = "bank-payment-service", url = "http://localhost:8080/api/v1/payment")
public interface PaymentClient {

    @GetMapping
    ResponseEntity<List<PaymentResponse>> getAllActivePayment();
}
