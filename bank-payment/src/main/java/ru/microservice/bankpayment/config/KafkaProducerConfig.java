package ru.microservice.bankpayment.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import ru.microservice.bankpayment.domain.Payment;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.payment.prop.bootstrapServers}")
    private String bootstrapServers;
    @Value("${kafka.payment.prop.retries}")
    private Integer retries;
    @Value("${kafka.payment.prop.deliveryTimeout}")
    private Integer deliveryTimeout;
    @Value("${kafka.payment.prop.requestTimeout}")
    private Integer requestTimeout;
    @Value("${kafka.payment.prop.maxInFlightRequests}")
    private Integer maxInFlightRequests;

    @Bean
    public ProducerFactory<String, Payment> producerFactory(
            JsonMapper jsonMapper
    ) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);



        JacksonJsonSerializer<Payment> serializer = new JacksonJsonSerializer<>(jsonMapper);

        DefaultKafkaProducerFactory<String, Payment> factory = new DefaultKafkaProducerFactory<>(
                configProps,
                new StringSerializer(),
                serializer);
        factory.setTransactionIdPrefix("bank-payment-tx-");
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Payment> kafkaTemplate(
            ProducerFactory<String, Payment> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
