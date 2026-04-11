package ru.microservice.bankpayment.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.microservice.bankpayment.domain.Payment;

@Service
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private final KafkaTemplate<String, Payment> kafkaTemplate;


    public void sendPaymentToKafka(Payment payment) {
        kafkaTemplate.send("payments", payment);
    }

}
