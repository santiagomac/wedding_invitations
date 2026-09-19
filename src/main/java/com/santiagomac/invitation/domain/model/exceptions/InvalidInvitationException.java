package com.santiagomac.invitation.domain.model.exceptions;

public class InvalidInvitationException extends RuntimeException {
    public InvalidInvitationException(String message) {
        super(message);
    }
}
