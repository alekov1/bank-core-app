package ru.microservice.bankaccount.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountTransferRequest (

    @NotBlank(message = "Счёт отправителя обязателен")
    String fromAccountNumber,

    @NotBlank(message = "Счёт получателя обязателен")
    String toAccountNumber,

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    BigDecimal amount)
{}
