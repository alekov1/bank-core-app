package ru.microservice.bankaccount.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bank-cashback-service", url = "http://localhost:8080/api/v1/cashback")
public interface CashbackClient {
}
