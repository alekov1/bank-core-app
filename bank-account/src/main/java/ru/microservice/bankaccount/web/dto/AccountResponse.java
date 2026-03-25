package ru.microservice.bankaccount.web.dto;

import ru.microservice.bankaccount.domain.enums.AccountCurrency;
import ru.microservice.bankaccount.domain.enums.AccountStatus;
import ru.microservice.bankaccount.domain.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponse(

        String accountNumber,

        BigDecimal balance,

        AccountCurrency currency,

        Integer creditLimit,

        AccountType accountType,

        AccountStatus status,

        String createdAt

) {
}
