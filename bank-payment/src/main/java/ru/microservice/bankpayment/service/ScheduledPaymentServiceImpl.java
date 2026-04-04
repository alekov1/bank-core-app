package ru.microservice.bankpayment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledPaymentServiceImpl implements ScheduledPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Override
    @Scheduled(fixedRate = 60_000)
    public void processScheduledPayments() {

        List<Payment> pending = paymentRepository.findByStatusAndScheduledAtBefore(
                PaymentStatus.PENDING,
                LocalDateTime.now()
        );
        if (pending.isEmpty())
            return;

        for (Payment payment : pending) {
            paymentService.executeScheduled(payment);
        }
    }
}
