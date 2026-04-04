package ru.microservice.bankpayment.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.microservice.bankpayment.domain.enums.RecipientType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRequest (

    @NotBlank(message = "Счёт отправителя обязателен")
    String fromAccountNumber,

    @NotBlank(message = "Счёт получателя обязателен")
    String toAccountNumber,

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    BigDecimal amount,

    @NotNull(message = "Тип получателя обязателен")
    RecipientType recipientType,

    LocalDateTime scheduledAt
) {
}
