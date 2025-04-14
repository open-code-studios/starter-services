package com.ocs.login.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final String errorCode;

    protected ApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public abstract HttpStatus getHttpStatus();
}