package ru.microservice.bankaccount.service;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.RequestParam;
import ru.microservice.bankaccount.db.AccountRepository;
import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankaccount.domain.enums.AccountStatus;
import ru.microservice.bankaccount.domain.enums.TransactionalStatus;
import ru.microservice.bankaccount.exception.AccountNotFoundException;
import ru.microservice.bankaccount.exception.InsufficientFundsException;
import ru.microservice.bankaccount.service.clientservice.NotificationAsyncService;
import ru.microservice.bankaccount.service.utils.GeneratorAccountsNumber;
import ru.microservice.bankaccount.web.dto.AccountRequest;
import ru.microservice.bankaccount.web.dto.AccountResponse;
import ru.microservice.bankaccount.web.dto.AccountTransferRequest;
import ru.microservice.bankaccount.web.dto.AccountTransferResponse;
import ru.microservice.bankaccount.web.dto.mapper.AccountMapper;
import ru.microservice.banknotification.domain.EmailType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final GeneratorAccountsNumber generator;
    private final AccountMapper accountMapper;
    private final NotificationAsyncService notificationAsyncService;

    @Timed("createAccount")
    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account.builder()

                .accountNumber(generator.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .currency(accountRequest.currency())
                .accountType(accountRequest.accountType())
                .creditLimit(0)
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        notificationAsyncService.sendNotificationAsync(EmailType.CREATE_ACCOUNT, account.getAccountNumber());
                    }
                }
        );

        return accountMapper.toResponse(account);
    }


    @Override
    public AccountResponse getAccountById(Long id) {
        return accountMapper.toResponse(accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id)));
    }

    @Timed("transfer")
    @Override
    @Transactional
    public AccountTransferResponse transferAccount(AccountTransferRequest request) {

        Account from = accountRepository.findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.fromAccountNumber()));
        Account to = accountRepository.findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.toAccountNumber()));

        if (from.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }
        try {
            from.setBalance(from.getBalance().subtract(request.amount()));
            to.setBalance(to.getBalance().add(request.amount()));

            accountRepository.save(from);
            accountRepository.save(to);

            return AccountTransferResponse.builder()
                    .fromAccountNumber(from.getAccountNumber())
                    .toAccountNumber(to.getAccountNumber())
                    .amount(request.amount())
                    .status(TransactionalStatus.SUCCESS.toString())
                    .error(null)
                    .build();
        } catch (Exception e) {
            return AccountTransferResponse.builder()
                    .fromAccountNumber(from.getAccountNumber())
                    .toAccountNumber(to.getAccountNumber())
                    .amount(request.amount())
                    .status(TransactionalStatus.FAILURE.toString())
                    .error(e.getMessage())
                    .build();
        }

    }

    public String getEmailByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).get().getEmail();
    }
}
