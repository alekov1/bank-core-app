package ru.microservice.bankcashback.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.microservice.bankcashback.domain.Payment;

@Slf4j
@Service
public class PaymentKafkaConsumer {

    @KafkaListener(topics = "payments")
    public void consumePayment(Payment payment) {
        log.info("Consuming payment {}", payment);
    }

}
