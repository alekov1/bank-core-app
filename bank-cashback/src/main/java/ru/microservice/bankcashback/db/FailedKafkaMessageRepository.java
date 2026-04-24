package ru.microservice.bankcashback.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.microservice.bankcashback.domain.FailedKafkaMessage;

@Repository
public interface FailedKafkaMessageRepository extends JpaRepository<FailedKafkaMessage, Long> {
}
