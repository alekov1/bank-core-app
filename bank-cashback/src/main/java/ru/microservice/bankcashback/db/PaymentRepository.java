package ru.microservice.bankcashback.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.microservice.bankcashback.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
