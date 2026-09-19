package com.santiagomac.invitation.infrastructure.entry_points;

import com.santiagomac.invitation.application.dto.InvitationResponse;
import com.santiagomac.invitation.application.dto.RsvpRequest;
import com.santiagomac.invitation.application.usecases.GetInvitationUseCase;
import com.santiagomac.invitation.application.usecases.RsvpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationPublicController {

    private final GetInvitationUseCase getInvitationUseCase;
    private final RsvpUseCase rsvpUseCase;

    @GetMapping("/{code}")
    public ResponseEntity<InvitationResponse> getInvitation(@PathVariable String code) {
        return ResponseEntity.ok(this.getInvitationUseCase.getByCode(code));
    }

    @PostMapping("/{code}/rsvp")
    public ResponseEntity<InvitationResponse> rsvp(@PathVariable String code, @RequestBody RsvpRequest request) {
        return ResponseEntity.ok(this.rsvpUseCase.rsvp(code, request));
    }
}
