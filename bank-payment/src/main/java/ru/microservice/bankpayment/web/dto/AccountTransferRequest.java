package ru.microservice.bankpayment.web.dto;

import java.math.BigDecimal;

public record AccountTransferRequest(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount
) {
}
