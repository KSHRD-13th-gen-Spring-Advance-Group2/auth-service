package com.kshrd.auth_service.exception;

public class CustomServiceUnavailableException extends RuntimeException {
    public CustomServiceUnavailableException(String message) {
        super(message);
    }
}
