package ru.microservice.bankaccount.service;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.microservice.bankaccount.web.dto.AccountRequest;
import ru.microservice.bankaccount.web.dto.AccountResponse;
import ru.microservice.bankaccount.web.dto.AccountTransferRequest;
import ru.microservice.bankaccount.web.dto.AccountTransferResponse;

public interface  AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccountById(Long id);

    AccountTransferResponse transferAccount(AccountTransferRequest request);

}
