package ru.microservice.bankpayment.web.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.microservice.bankpayment.service.PaymentService;
import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> transfer(
            @RequestBody @Valid PaymentRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.transfer(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllActivePayment(){
        return null;
    }

}
