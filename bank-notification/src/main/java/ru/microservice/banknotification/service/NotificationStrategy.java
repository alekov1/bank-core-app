package ru.microservice.banknotification.service;

import org.springframework.http.ResponseEntity;
import ru.microservice.banknotification.domain.EmailType;

public interface NotificationStrategy {

    ResponseEntity<String> sendEmail(String email);

    EmailType getEmailType();

}
