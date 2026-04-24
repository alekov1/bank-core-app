package ru.microservice.bankcashback.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservice.bankcashback.db.FailedKafkaMessageRepository;
import ru.microservice.bankcashback.db.PaymentRepository;
import ru.microservice.bankcashback.db.ProcessedKafkaMessageRepository;
import ru.microservice.bankcashback.domain.FailedKafkaMessage;
import ru.microservice.bankcashback.domain.Payment;
import ru.microservice.bankcashback.domain.ProcessedKafkaMessage;
import ru.microservice.bankcashback.exception.NonRetryableException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentKafkaConsumer {

    private final PaymentRepository paymentRepository;
    private final ProcessedKafkaMessageRepository processedKafkaMessageRepository;
    private final FailedKafkaMessageRepository failedKafkaMessageRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payments")
    @Transactional
    public void consumePayment(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String rawData = record.value();
        String messageId = resolveMessageId(record);

        if (processedKafkaMessageRepository.existsByMessageId(messageId)) {
            acknowledgment.acknowledge();
            return;
        }

        Payment payment;
        try {
            payment = objectMapper.readValue(rawData, Payment.class);
        } catch (Exception e) {
            saveFailedMessage(messageId, record, rawData, e.getMessage());
            acknowledgment.acknowledge();
            log.error("Failed to deserialize payment, messageId={}", messageId, e);
            throw new NonRetryableException("Invalid JSON payload", e);
        }

        try {
            paymentRepository.save(payment);
            processedKafkaMessageRepository.save(ProcessedKafkaMessage.builder()
                    .messageId(messageId)
                    .topic(record.topic())
                    .partitionNumber(record.partition())
                    .offsetValue(record.offset())
                    .processedAt(LocalDateTime.now())
                    .build());
            acknowledgment.acknowledge();
            log.info("Payment consumed successfully, messageId={}", messageId);
        } catch (Exception ex) {
            saveFailedMessage(messageId, record, rawData, ex.getMessage());
            acknowledgment.acknowledge();
            log.error("Failed to consume payment, messageId={}", messageId, ex);
        }
    }

    private void saveFailedMessage(String messageId, ConsumerRecord<String, String> record,
                                   String payload, String errorMessage) {
        failedKafkaMessageRepository.save(FailedKafkaMessage.builder()
                .messageId(messageId)
                .topic(record.topic())
                .partitionNumber(record.partition())
                .offsetValue(record.offset())
                .payload(payload)
                .errorMessage(errorMessage)
                .failedAt(LocalDateTime.now())
                .build());
    }

    private String resolveMessageId(ConsumerRecord<String, String> record) {
        if (record.headers().lastHeader("eventId") != null) {
            return new String(record.headers().lastHeader("eventId").value(), StandardCharsets.UTF_8);
        }
        return record.topic() + "-" + record.partition() + "-" + record.offset();
    }
}