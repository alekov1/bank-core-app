package ru.microservice.bankaccount.web.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.microservice.bankaccount.service.AccountService;
import ru.microservice.bankaccount.web.dto.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody @Valid AccountRequest accountRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountRequest));
    }

    @GetMapping("/email")
    public String getEmailByAccountNumber(@RequestParam("accountNumber") String accountNumber) {
        return accountService.getEmailByAccountNumber(accountNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountTransferResponse> transferAccount(@RequestBody @Valid AccountTransferRequest accountRequest) {
        return ResponseEntity.ok(accountService.transferAccount(accountRequest));
    }

    @PostMapping("/{accountNumber}/block")
    public ResponseEntity<BlockResponse> blockAccount(@PathVariable String accountNumber,
                                                      @RequestBody BlockRequest request) {

        CompletableFuture<BlockResponse> future = accountService.blockAccount(accountNumber);

        try {
            BlockResponse response = future.get(5, TimeUnit.SECONDS);
            return ResponseEntity.ok(response);
        } catch (TimeoutException e) {
            return ResponseEntity.status(504).body(new BlockResponse(false, "Сервисы временно недоступны"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new BlockResponse(false, "Ошибка: " + e.getMessage()));
        }
    }




}
