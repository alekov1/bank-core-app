package ru.microservice.bankpayment.web.dto;

import ru.microservice.bankpayment.domain.enums.PaymentStatus;
import ru.microservice.bankpayment.domain.enums.RecipientType;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        PaymentStatus status,
        RecipientType recipientType,
        String failureReason,
        String createdAt
) {}