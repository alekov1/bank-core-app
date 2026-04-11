package ru.microservice.bankpayment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.microservice.bankpayment.web.dto.AccountTransferRequest;


@Component
@FeignClient(name = "bank-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {

    @PostMapping("/transfer")
    void transfer(@RequestBody AccountTransferRequest request);


}
