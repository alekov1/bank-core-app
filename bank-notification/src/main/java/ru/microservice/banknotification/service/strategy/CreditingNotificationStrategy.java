package ru.microservice.banknotification.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.microservice.banknotification.db.NotificationTemplateRepository;
import ru.microservice.banknotification.domain.EmailType;
import ru.microservice.banknotification.domain.NotificationTemplate;
import ru.microservice.banknotification.exception.NotFoundNotificationException;
import ru.microservice.banknotification.service.EmailSender;
import ru.microservice.banknotification.service.NotificationStrategy;

@Service
@RequiredArgsConstructor
public class CreditingNotificationStrategy implements NotificationStrategy {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final EmailSender emailSender;


    @Override
    public ResponseEntity<String> sendEmail(String email) {

        NotificationTemplate template = notificationTemplateRepository
                .findByType(EmailType.CREDITING)
                .orElseThrow(()-> new NotFoundNotificationException("Шаблон уведомления не найден"));
        emailSender.send(email, template.getMessage());
        return ResponseEntity.ok().build();
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.CREDITING;
    }
}
