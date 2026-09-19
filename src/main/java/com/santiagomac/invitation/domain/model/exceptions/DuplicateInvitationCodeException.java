package com.santiagomac.invitation.domain.model.exceptions;

public class DuplicateInvitationCodeException extends RuntimeException {
    public DuplicateInvitationCodeException(String message) {
        super(message);
    }
}
