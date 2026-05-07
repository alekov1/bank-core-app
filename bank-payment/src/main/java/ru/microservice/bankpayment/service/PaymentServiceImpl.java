package ru.microservice.bankpayment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.microservice.banknotification.domain.EmailType;
import ru.microservice.bankpayment.client.AccountClient;
import ru.microservice.bankpayment.db.PaymentRepository;
import ru.microservice.bankpayment.domain.Payment;
import ru.microservice.bankpayment.domain.enums.PaymentStatus;
import ru.microservice.bankpayment.service.clientservice.NotificationAsyncService;
import ru.microservice.bankpayment.service.kafka.PaymentOutboxService;
import ru.microservice.bankpayment.web.dto.AccountTransferRequest;
import ru.microservice.bankpayment.web.dto.PaymentRequest;
import ru.microservice.bankpayment.web.dto.PaymentResponse;
import ru.microservice.bankpayment.web.dto.mapper.PaymentMapper;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final AccountClient accountClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentOutboxService paymentOutboxService;
    private final NotificationAsyncService notificationAsyncService;
    private final TransactionCallbackService transactionCallbackService ;

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
        scheduledPayment.setScheduledAt(LocalDateTime.now());
        return paymentMapper.toResponse(paymentRepository.save(scheduledPayment));

    }

    @Override
    @Transactional
    public void executeScheduled(Payment payment) {

        try {
            accountClient.transfer(new AccountTransferRequest(
                    payment.getFromAccountNumber(),
                    payment.getToAccountNumber(),
                    payment.getAmount()
            ));

            payment.setStatus(PaymentStatus.SUCCESS);
            Payment saved = paymentRepository.save(payment);
            paymentOutboxService.saveEvent(saved);

            transactionCallbackService.afterCommit(List.of(
                    () -> notificationAsyncService.sendNotificationAsync(EmailType.DEBITING, payment.getFromAccountNumber()),
                    () -> notificationAsyncService.sendNotificationAsync(EmailType.CREDITING, payment.getToAccountNumber())
            ));

        } catch (Exception ex) {

            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

        }
    }

    private PaymentResponse executeTransfer(PaymentRequest request) {

        try {
            accountClient.transfer(new AccountTransferRequest(
                    request.fromAccountNumber(),
                    request.toAccountNumber(),
                    request.amount()
            ));

            Payment payment = buildPayment(request, PaymentStatus.SUCCESS);
            Payment saved = paymentRepository.save(payment);
            paymentOutboxService.saveEvent(saved);

            transactionCallbackService.afterCommit(List.of(
                    () -> notificationAsyncService.sendNotificationAsync(EmailType.DEBITING, payment.getFromAccountNumber()),
                    () -> notificationAsyncService.sendNotificationAsync(EmailType.CREDITING, payment.getToAccountNumber())
            ));

            return paymentMapper.toResponse(saved);

        } catch (Exception e) {
            log.error("Ошибка платежа: ", e.getMessage());
            Payment payment = buildPayment(request, PaymentStatus.FAILED);
            Payment saved = paymentRepository.save(payment);
            return paymentMapper.toResponse(saved);
        }

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
