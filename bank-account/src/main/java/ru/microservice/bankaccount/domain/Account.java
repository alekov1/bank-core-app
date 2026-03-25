package ru.microservice.bankaccount.domain;


import jakarta.persistence.*;
import lombok.*;
import ru.microservice.bankaccount.domain.enums.AccountStatus;
import ru.microservice.bankaccount.domain.enums.AccountCurrency;
import ru.microservice.bankaccount.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "accounts", schema = "account_schema")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountCurrency currency;

    private Integer creditLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime closedAt;



}
