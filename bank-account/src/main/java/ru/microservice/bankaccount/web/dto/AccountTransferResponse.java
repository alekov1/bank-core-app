package ru.microservice.bankaccount.web.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountTransferResponse(

        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        String status,
        String error

) {


}
