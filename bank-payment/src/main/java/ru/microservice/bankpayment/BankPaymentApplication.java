package ru.microservice.bankpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.microservice.bankpayment")
@EnableFeignClients(basePackages = "ru.microservice.bankpayment.client")
public class BankPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankPaymentApplication.class, args);
    }

}
