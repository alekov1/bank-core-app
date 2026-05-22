package ru.microservice.bankaccount.web.dto;

public record BlockValidationResult(
        boolean hasActivePayments,
        boolean hasActiveCashbacks
){}
