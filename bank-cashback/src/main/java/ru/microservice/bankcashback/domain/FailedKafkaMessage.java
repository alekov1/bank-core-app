package ru.microservice.bankcashback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_kafka_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedKafkaMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String messageId;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private Integer partitionNumber;

    @Column(nullable = false)
    private Long offsetValue;

    @Lob
    private String payload;

    @Lob
    @Column(nullable = false)
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime failedAt;
}
