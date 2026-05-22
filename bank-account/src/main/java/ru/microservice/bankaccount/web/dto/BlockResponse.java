package ru.microservice.bankaccount.web.dto;

public record BlockResponse(
        boolean result,
        String reason
) {
}
