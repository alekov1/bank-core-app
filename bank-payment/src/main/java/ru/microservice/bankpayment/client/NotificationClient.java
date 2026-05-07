package ru.microservice.bankpayment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.microservice.bankaccount.web.dto.NotificationRequest;
import ru.microservice.banknotification.domain.EmailType;

@FeignClient(name = "bank-notification-service", url = "http://localhost:8080/api/v1/notify")
public interface NotificationClient {

    @PostMapping
    ResponseEntity<String> notify(@RequestBody NotificationRequest notificationRequest);
}
