package ru.microservice.bankpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "ru.microservice.bankpayment",
        "ru.microservice.bankaccount"
})
@EnableJpaRepositories(basePackages = {
        "ru.microservice.bankpayment.db",
        "ru.microservice.bankaccount.db"
})
@EntityScan(basePackages = {
        "ru.microservice.bankpayment.domain",
        "ru.microservice.bankaccount.domain"
})
public class BankPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankPaymentApplication.class, args);
    }

}
