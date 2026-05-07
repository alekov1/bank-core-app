package ru.microservice.bankaccount.web.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.microservice.bankaccount.service.AccountService;
import ru.microservice.bankaccount.web.dto.AccountRequest;
import ru.microservice.bankaccount.web.dto.AccountResponse;
import ru.microservice.bankaccount.web.dto.AccountTransferRequest;
import ru.microservice.bankaccount.web.dto.AccountTransferResponse;

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




}
