package ru.microservice.bankpayment.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStatusAndScheduledAtBefore(
            PaymentStatus status,
            LocalDateTime scheduledAt
    );

}
