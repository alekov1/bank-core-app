package ru.microservice.bankcashback.service.clientservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.microservice.bankcashback.client.NotificationClient;
import ru.microservice.bankcashback.web.dto.NotificationRequest;
import ru.microservice.banknotification.domain.EmailType;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAsyncService {

    private final NotificationClient notificationClient;

    @Async
    public CompletableFuture<Void> sendNotificationAsync(EmailType emailType, String accountNumber) {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .emailType(emailType)
                .accountNumber(accountNumber)
                .build();

        try {
            notificationClient.notify(notificationRequest);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return CompletableFuture.completedFuture(null);
    }

}
