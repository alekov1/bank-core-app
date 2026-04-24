package ru.microservice.bankcashback.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.microservice.bankcashback.domain.ProcessedKafkaMessage;

@Repository
public interface ProcessedKafkaMessageRepository extends JpaRepository<ProcessedKafkaMessage, Long> {

    boolean existsByMessageId(String messageId);
}
