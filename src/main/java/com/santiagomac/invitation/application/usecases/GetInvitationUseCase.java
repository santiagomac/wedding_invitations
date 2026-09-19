package com.santiagomac.invitation.application.usecases;

import com.santiagomac.invitation.application.dto.InvitationResponse;
import com.santiagomac.invitation.application.ports.out.InvitationGateway;
import com.santiagomac.invitation.domain.model.exceptions.InvitationNotFoundException;
import com.santiagomac.invitation.domain.model.invitation.InvitationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetInvitationUseCase {

    private final InvitationGateway invitationGateway;

    public InvitationResponse getByCode(String code) {
        InvitationModel invitation = invitationGateway.findByCode(code)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found: " + code));

        return toResponse(invitation);
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
