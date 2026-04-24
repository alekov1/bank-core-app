package ru.microservice.bankpayment.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservice.bankpayment.db.PaymentOutboxRepository;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.PaymentOutboxEvent;
import ru.microservice.bankpayment.domain.enums.OutboxStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final int MAX_ATTEMPTS = 5;

    private final PaymentOutboxRepository paymentOutboxRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentKafkaProducer paymentKafkaProducer;

    @Scheduled(fixedDelay = 5_000)
    public void publish() {
        List<PaymentOutboxEvent> events = paymentOutboxRepository.findReadyForPublishing(
                List.of(OutboxStatus.NEW, OutboxStatus.FAILED),
                LocalDateTime.now()
        );

        for (PaymentOutboxEvent event : events) {
            publishEvent(event);
        }
    }

    @Transactional
    public void publishEvent(PaymentOutboxEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new IllegalStateException("Payment not found: " + event.getPaymentId()));
        try {
            paymentKafkaProducer.sendPaymentToKafka(event, payment);
            event.setStatus(OutboxStatus.SENT);
            event.setSentAt(LocalDateTime.now());
            event.setErrorMessage(null);
            paymentOutboxRepository.save(event);
        } catch (Exception ex) {
            int nextAttempt = event.getAttempts() + 1;
            event.setAttempts(nextAttempt);
            event.setStatus(OutboxStatus.FAILED);
            event.setErrorMessage(ex.getMessage());
            if (nextAttempt >= MAX_ATTEMPTS) {
                event.setNextRetryAt(LocalDateTime.now().plusHours(1));
            } else {
                event.setNextRetryAt(LocalDateTime.now().plusSeconds(10L * nextAttempt));
            }
            paymentOutboxRepository.save(event);
            log.error("Failed to publish outbox event {}", event.getEventId(), ex);
        }
    }
}
