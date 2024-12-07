package com.accenture.exception;

import lombok.Getter;

@Getter
public class ApiError {

    private final int status;
    private final String error;
    private final String message;

    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

}
