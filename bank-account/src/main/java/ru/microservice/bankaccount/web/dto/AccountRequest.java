package ru.microservice.bankaccount.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import ru.microservice.bankaccount.domain.enums.AccountCurrency;
import ru.microservice.bankaccount.domain.enums.AccountType;

public record AccountRequest(

        @NotNull(message = "Валюта обязательна")
        AccountCurrency currency,

        @NotNull(message = "Тип счёта обязателен")
        AccountType accountType,

        @Min(value = 0, message = "Кредитный лимит не может быть отрицательным")
        Integer creditLimit
) {
}
