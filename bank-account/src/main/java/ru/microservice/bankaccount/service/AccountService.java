package ru.microservice.bankaccount.service;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.microservice.bankaccount.web.dto.*;

import java.util.concurrent.CompletableFuture;

public interface  AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccountById(Long id);

    AccountTransferResponse transferAccount(AccountTransferRequest request);

    String getEmailByAccountNumber(String accountNumber);
    CompletableFuture<BlockResponse> blockAccount(String accountNumber);
}
