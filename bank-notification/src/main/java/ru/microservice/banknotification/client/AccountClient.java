package ru.microservice.banknotification.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "bank-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {

    @PostMapping("/email")
    String getEmailByAccountNumber(@RequestParam("accountNumber") String accountNumber);

}
