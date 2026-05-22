package ru.microservice.testmodule.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicAccountService implements AccountService {

    private final Map<String, AtomicReference<BigDecimal>> accounts = new ConcurrentHashMap<>();

    public AtomicAccountService() {
        accounts.put("ACC-001", new AtomicReference<>(BigDecimal.valueOf(1000)));
        accounts.put("ACC-002", new AtomicReference<>(BigDecimal.valueOf(500)));
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        AtomicReference<BigDecimal> ref = accounts.get(accountNumber);
        return ref != null ? ref.get() : BigDecimal.ZERO;
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        accounts.computeIfAbsent(accountNumber, k -> new AtomicReference<>(BigDecimal.ZERO))
                .updateAndGet(current -> current.add(amount));
    }

    @Override
    public void withdraw(String accountNumber, BigDecimal amount) {
        AtomicReference<BigDecimal> ref = accounts.get(accountNumber);
        if (ref != null) {
            ref.updateAndGet(current -> {
                if (current.compareTo(amount) >= 0) {
                    return current.subtract(amount);
                }
                return current;
            });
        }
    }
}
