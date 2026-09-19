package com.santiagomac.invitation.infrastructure.entry_points;

import com.santiagomac.invitation.domain.model.exceptions.DuplicateInvitationCodeException;
import com.santiagomac.invitation.domain.model.exceptions.InvalidGuestCountException;
import com.santiagomac.invitation.domain.model.exceptions.InvalidInvitationException;
import com.santiagomac.invitation.domain.model.exceptions.InvalidRsvpTransitionException;
import com.santiagomac.invitation.domain.model.exceptions.InvitationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class InvitationExceptionHandler {

    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(InvitationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidRsvpTransitionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTransition(InvalidRsvpTransitionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({InvalidGuestCountException.class, InvalidInvitationException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateInvitationCodeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCode(DuplicateInvitationCodeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}
