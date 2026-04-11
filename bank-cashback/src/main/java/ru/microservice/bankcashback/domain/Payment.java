package ru.microservice.bankcashback.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.microservice.bankcashback.domain.enums.PaymentStatus;
import ru.microservice.bankcashback.domain.enums.RecipientType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fromAccountNumber;

    @Column(nullable = false)
    private String toAccountNumber;

    @Column(nullable = false)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipientType recipientType;

    private String failureReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime scheduledAt;
}
