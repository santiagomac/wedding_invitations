package com.santiagomac.invitation.domain.model.exceptions;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(String message) {
        super(message);
    }
}
