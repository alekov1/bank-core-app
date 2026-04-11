package ru.microservice.bankaccount.web.dto;

import java.math.BigDecimal;

public record AccountTransferResponse(

        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        String status,
        String error

) {


}
