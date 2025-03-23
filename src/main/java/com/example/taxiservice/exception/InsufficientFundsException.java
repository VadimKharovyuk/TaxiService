package com.example.taxiservice.exception;

/**
 * Исключение, выбрасываемое при недостаточном балансе пользователя
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}