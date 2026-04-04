package ru.microservice.bankaccount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.microservice.bankaccount.db.AccountRepository;
import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankaccount.domain.enums.AccountStatus;
import ru.microservice.bankaccount.exception.AccountNotFoundException;
import ru.microservice.bankaccount.service.utils.GeneratorAccountsNumber;
import ru.microservice.bankaccount.web.dto.AccountRequest;
import ru.microservice.bankaccount.web.dto.AccountResponse;
import ru.microservice.bankaccount.web.dto.mapper.AccountMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final GeneratorAccountsNumber generator;
    private final AccountMapper accountMapper;

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

        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        return accountMapper.toResponse(accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id)));
    }

}
