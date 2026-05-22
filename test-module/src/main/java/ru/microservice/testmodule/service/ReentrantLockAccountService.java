package ru.microservice.testmodule.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockAccountService implements AccountService {

    private final Map<String, BigDecimal> accounts = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public ReentrantLockAccountService() {
        accounts.put("ACC-001", BigDecimal.valueOf(1000));
        accounts.put("ACC-002", BigDecimal.valueOf(500));
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        lock.lock();
        try {
            return accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        lock.lock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            accounts.put(accountNumber, current.add(amount));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        lock.lock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            if (current.compareTo(amount) >= 0) {
                accounts.put(accountNumber, current.subtract(amount));
            }
        } finally {
            lock.unlock();
        }
    }
}
