package ru.microservice.banknotification.exception;

public class NotFoundNotificationException extends RuntimeException {
    public NotFoundNotificationException(String message) {
        super(message);
    }
}
