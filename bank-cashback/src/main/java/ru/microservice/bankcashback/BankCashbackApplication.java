package ru.microservice.bankcashback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BankCashbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCashbackApplication.class, args);
    }

}
