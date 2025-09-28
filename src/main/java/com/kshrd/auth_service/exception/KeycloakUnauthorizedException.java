package com.kshrd.auth_service.exception;

import lombok.Getter;

public class KeycloakUnauthorizedException extends RuntimeException {
    @Getter
    private String title = null;

    public KeycloakUnauthorizedException(String message) {
        super(message);
    }

    public KeycloakUnauthorizedException(String message, String title) {
        super(message);
        this.title = title;
    }
}
