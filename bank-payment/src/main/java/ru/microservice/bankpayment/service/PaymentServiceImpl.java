package ru.microservice.bankpayment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservice.bankpayment.client.AccountClient;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;
import ru.microservice.bankpayment.exception.InsufficientFundsException;
import ru.microservice.bankpayment.web.dto.AccountDto;
import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;
import ru.microservice.bankpayment.web.dto.mapper.PaymentMapper;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountClient accountClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse transfer(PaymentRequest request) {

        if (request.scheduledAt() == null) {
            return scheduledPayment(request);
        }
        return executeTransfer(request);
    }

    private PaymentResponse scheduledPayment(PaymentRequest request) {
        Payment scheduledPayment = buildPayment(request, PaymentStatus.PENDING);
        return paymentMapper.toResponse(paymentRepository.save(scheduledPayment));

    }

    @Override
    @Transactional
    public void executeScheduled(Payment payment) {
        executeTransfer(paymentMapper.toRequest(payment));
    }

    private PaymentResponse executeTransfer(PaymentRequest request) {

        AccountDto from = accountClient.getAccountByNumber(request.fromAccountNumber());
        AccountDto to = accountClient.getAccountByNumber(request.toAccountNumber());

        if (from.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));

        accountClient.updateBalance(from.getAccountNumber(), request.amount().negate());
        accountClient.updateBalance(to.getAccountNumber(), request.amount());

        Payment payment = buildPayment(request, PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);

        return paymentMapper.toResponse(saved);
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


}
