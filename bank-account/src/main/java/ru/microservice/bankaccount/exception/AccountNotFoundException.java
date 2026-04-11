package ru.microservice.bankaccount.exception;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Счёт с ID " + id + " не найден");
    }

    public AccountNotFoundException(@NotBlank(message = "Счёт отправителя обязателен") String s) {
        super("Счёт с номером " + s + " не найден");
    }
}