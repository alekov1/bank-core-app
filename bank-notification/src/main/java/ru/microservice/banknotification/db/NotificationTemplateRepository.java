package ru.microservice.banknotification.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.microservice.banknotification.domain.EmailType;
import ru.microservice.banknotification.domain.NotificationTemplate;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByType(EmailType type);
}

