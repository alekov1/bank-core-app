package ru.microservice.testmodule.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockAccountService implements AccountService {

    private final Map<String, BigDecimal> accounts = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public ReadWriteLockAccountService() {
        accounts.put("ACC-001", BigDecimal.valueOf(1000));
        accounts.put("ACC-002", BigDecimal.valueOf(500));
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        readLock.lock();
        try {
            return accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        writeLock.lock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            accounts.put(accountNumber, current.add(amount));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        writeLock.lock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            if (current.compareTo(amount) >= 0) {
                accounts.put(accountNumber, current.subtract(amount));
            }
        } finally {
            writeLock.unlock();
        }
    }
}
