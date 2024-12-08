package com.accenture.exception;

public class OrderAlreadyProcessedException extends RuntimeException {

    public OrderAlreadyProcessedException(String message) {
        super(message);
    }

    public OrderAlreadyProcessedException(String message, Throwable cause) {
        super(message, cause);
    }
}
