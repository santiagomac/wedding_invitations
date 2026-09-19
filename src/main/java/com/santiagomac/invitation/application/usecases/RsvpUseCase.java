package com.santiagomac.invitation.application.usecases;

import com.santiagomac.invitation.application.dto.InvitationResponse;
import com.santiagomac.invitation.application.dto.RsvpRequest;
import com.santiagomac.invitation.application.ports.out.InvitationGateway;
import com.santiagomac.invitation.domain.model.exceptions.InvitationNotFoundException;
import com.santiagomac.invitation.domain.model.invitation.InvitationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RsvpUseCase {

    private final InvitationGateway invitationGateway;

    public InvitationResponse rsvp(String code, RsvpRequest request) {
        InvitationModel invitation = invitationGateway.findByCode(code)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found: " + code));

        if (request.isAttending()) {
            invitation.confirm(request.getConfirmedGuests());
        } else {
            invitation.decline();
        }

        InvitationModel updated = invitationGateway.save(invitation);
        return toResponse(updated);
    }

    private InvitationResponse toResponse(InvitationModel invitation) {
        return InvitationResponse.builder()
                .code(invitation.getCode())
                .displayName(invitation.getDisplayName())
                .maxGuests(invitation.getMaxGuests())
                .status(invitation.getStatus())
                .confirmedGuests(invitation.getConfirmedGuests())
                .respondedAt(invitation.getRespondedAt())
                .build();
    }
}
