package ru.microservice.bankpayment.service;

import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse transfer(PaymentRequest request);

}
