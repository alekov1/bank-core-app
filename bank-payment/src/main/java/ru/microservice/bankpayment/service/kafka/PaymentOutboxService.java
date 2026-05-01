package ru.microservice.bankpayment.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.microservice.bankpayment.db.PaymentOutboxRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.PaymentOutboxEvent;
import ru.microservice.bankpayment.domain.enums.OutboxStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentOutboxService {

    private final PaymentOutboxRepository paymentOutboxRepository;

    @Value("${send.kafka.topic}")
    private String topic;

    public void saveEvent(Payment payment) {
        PaymentOutboxEvent event = PaymentOutboxEvent.builder()
                .eventId(UUID.randomUUID())
                .paymentId(payment.getId())
                .topic(topic)
                .status(OutboxStatus.NEW)
                .attempts(0)
                .nextRetryAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        paymentOutboxRepository.save(event);
    }
}
