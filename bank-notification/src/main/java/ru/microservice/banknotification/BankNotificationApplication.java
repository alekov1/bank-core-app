package ru.microservice.banknotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ru.microservice.banknotification")
@EnableFeignClients
public class BankNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankNotificationApplication.class, args);
    }

}
