package ru.microservice.bankpayment.service.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.PaymentOutboxEvent;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private final KafkaTemplate<String, Payment> kafkaTemplate;


    public void sendPaymentToKafka(PaymentOutboxEvent event, Payment payment) {
        kafkaTemplate.executeInTransaction(operations -> {
            ProducerRecord<String, Payment> record = new ProducerRecord<>(
                    event.getTopic(),
                    String.valueOf(payment.getId()),
                    payment
            );
            record.headers().add(new RecordHeader(
                    "eventId",
                    event.getEventId().toString().getBytes(StandardCharsets.UTF_8)
            ));
            operations.send(record).join();
            return null;
        });
    }

}
