package ru.microservice.bankpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "ru.microservice.bankpayment")
@EnableFeignClients(basePackages = "ru.microservice.bankpayment.client")
@EnableScheduling
@EnableAsync
public class BankPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankPaymentApplication.class, args);
    }

}
