package ru.microservice.testmodule.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SynchronizedAccountService implements AccountService {

    private final Map<String, BigDecimal> accounts = new ConcurrentHashMap<>();

    public SynchronizedAccountService() {
        accounts.put("ACC-001", BigDecimal.valueOf(1000));
        accounts.put("ACC-002", BigDecimal.valueOf(500));
    }

    @Override
    public synchronized BigDecimal getBalance(String accountNumber) {
        return accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        synchronized (this) {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            accounts.put(accountNumber, current.add(amount));
        }
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        synchronized (this) {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            if (current.compareTo(amount) >= 0) {
                accounts.put(accountNumber, current.subtract(amount));
            }
        }
    }
}
