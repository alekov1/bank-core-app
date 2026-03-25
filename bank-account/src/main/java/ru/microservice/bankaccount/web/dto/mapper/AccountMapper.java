package ru.microservice.bankaccount.web.dto.mapper;

import ru.microservice.bankaccount.domain.Account;
import ru.microservice.bankaccount.web.dto.AccountResponse;


public interface AccountMapper {

    AccountResponse toResponse(Account account);
}
