package com.accenture.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
