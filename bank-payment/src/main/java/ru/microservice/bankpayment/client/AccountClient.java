package ru.microservice.bankpayment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.microservice.bankpayment.web.dto.AccountDto;

import java.math.BigDecimal;


@Component
@FeignClient(name = "bank-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {


    @GetMapping("/{accountNumber}")
    AccountDto getAccountByNumber(@PathVariable("accountNumber") String accountNumber);

    @PutMapping("/{accountNumber}/balance")
    void updateBalance(@PathVariable("accountNumber") String accountNumber,
                       @RequestParam("amount") BigDecimal amount);
}
