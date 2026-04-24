package ru.microservice.bankpayment.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.microservice.bankpayment.domain.PaymentOutboxEvent;
import ru.microservice.bankpayment.domain.enums.OutboxStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxEvent, Long> {

    @Query("""
            SELECT e
            FROM PaymentOutboxEvent e
            WHERE e.status IN :statuses
              AND e.nextRetryAt <= :retryAt
            ORDER BY e.createdAt ASC
            """)
    List<PaymentOutboxEvent> findReadyForPublishing(
            @Param("statuses") List<OutboxStatus> statuses,
            @Param("retryAt") LocalDateTime retryAt
    );
}
