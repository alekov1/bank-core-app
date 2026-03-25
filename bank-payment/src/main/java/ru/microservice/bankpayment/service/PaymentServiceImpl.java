package ru.microservice.bankpayment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservice.bankaccount.db.AccountRepository;
import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;
import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public PaymentResponse transfer(PaymentRequest request) {

        Account from = accountRepository.findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Счет отправителя не найден: " + request.fromAccountNumber()
                ));

        Account to = accountRepository.findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Счет получателя не найден: " + request.fromAccountNumber()
                ));

        if (from.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Недостаточно средств");
        }

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));

        accountRepository.save(from);
        accountRepository.save(to);

        Payment payment = buildPayment(request, PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

        return toResponse(saved);
    }

    private Payment buildPayment(PaymentRequest request, PaymentStatus status) {
        return Payment.builder()
                .fromAccountNumber(request.fromAccountNumber())
                .toAccountNumber(request.toAccountNumber())
                .amount(request.amount())
                .status(status)
                .recipientType(request.recipientType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getFromAccountNumber(),
                payment.getToAccountNumber(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getRecipientType(),
                payment.getFailureReason(),
                payment.getCreatedAt().format(FORMATTER)
        );
    }
}
