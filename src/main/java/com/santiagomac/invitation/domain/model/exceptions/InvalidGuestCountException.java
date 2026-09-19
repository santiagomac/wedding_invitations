package com.santiagomac.invitation.domain.model.exceptions;

public class InvalidGuestCountException extends RuntimeException {
    public InvalidGuestCountException(String message) {
        super(message);
    }
}
