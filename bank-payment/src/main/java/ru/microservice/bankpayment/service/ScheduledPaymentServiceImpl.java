package ru.microservice.bankpayment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledPaymentServiceImpl {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;


    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void processScheduledPayments() {

        try {
            List<Payment> pending = paymentRepository.findByStatusAndScheduledAtBeforeWithLock(
                    PaymentStatus.PENDING,
                    LocalDateTime.now()
            );

            if (pending.isEmpty())
                return;

            for (Payment payment : pending) {
                try {
                    if (payment.getStatus() == PaymentStatus.PENDING) {
                        payment.setStatus(PaymentStatus.PROCESSING);
                        paymentService.executeScheduled(payment);
                    }
                } catch (Exception e) {
                    log.error("Ошибка: ", e);
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
            }
        } catch (PessimisticLockingFailureException e) {
            log.error("Блокировка не получена", e);
        }

    }
}
