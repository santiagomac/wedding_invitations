package com.santiagomac.auth.domain.model.exceptions;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound(String message) {
        super(message);
    }
}
