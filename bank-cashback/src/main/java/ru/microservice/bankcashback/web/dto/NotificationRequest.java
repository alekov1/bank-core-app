package ru.microservice.bankcashback.web.dto;

import lombok.Builder;
import ru.microservice.banknotification.domain.EmailType;

@Builder
public record NotificationRequest(
        EmailType emailType,
        String accountNumber
) {
}
