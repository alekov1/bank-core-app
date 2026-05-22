package ru.microservice.testmodule.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

public class StampedLockAccountService implements AccountService {

    private final Map<String, BigDecimal> accounts = new ConcurrentHashMap<>();
    private final StampedLock stampedLock = new StampedLock();

    public StampedLockAccountService() {
        accounts.put("ACC-001", BigDecimal.valueOf(1000));
        accounts.put("ACC-002", BigDecimal.valueOf(500));
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        long stamp = stampedLock.tryOptimisticRead();
        BigDecimal balance = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                balance = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return balance;
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        long stamp = stampedLock.writeLock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            accounts.put(accountNumber, current.add(amount));
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        long stamp = stampedLock.writeLock();
        try {
            BigDecimal current = accounts.getOrDefault(accountNumber, BigDecimal.ZERO);
            if (current.compareTo(amount) >= 0) {
                accounts.put(accountNumber, current.subtract(amount));
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
