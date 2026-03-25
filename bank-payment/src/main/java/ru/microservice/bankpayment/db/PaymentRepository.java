package ru.microservice.bankpayment.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.microservice.bankpayment.domain.Payment;

import java.util.List;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFromAccountNumber(String accountNumber);
    List<Payment> findByToAccountNumber(String accountNumber);

}
