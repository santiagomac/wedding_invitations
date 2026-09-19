package com.santiagomac.invitation.domain.model.exceptions;

public class InvalidRsvpTransitionException extends RuntimeException {
    public InvalidRsvpTransitionException(String message) {
        super(message);
    }
}
