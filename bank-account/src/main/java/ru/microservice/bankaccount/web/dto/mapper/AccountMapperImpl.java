package ru.microservice.bankaccount.web.dto.mapper;

import org.springframework.stereotype.Component;
import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankaccount.web.dto.AccountResponse;

import java.time.format.DateTimeFormatter;

@Component
public class AccountMapperImpl implements AccountMapper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreditLimit() != null
                        ? account.getCreditLimit()
                        : null,
                account.getAccountType(),
                account.getStatus(),
                account.getCreatedAt().format(FORMATTER)
        );
    }
}
