package ru.microservice.bankaccount.service.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneratorAccountsNumber {

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder("4080");
        for (int i = 0; i < 16; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return sb.toString();
    }
}
