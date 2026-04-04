package ru.microservice.bankpayment.exception;



public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountNumber) {
        super("Счёт не найден: " + accountNumber);
    }
}