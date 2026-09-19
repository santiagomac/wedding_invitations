package com.santiagomac.invitation.application.usecases;

import com.santiagomac.invitation.application.dto.InvitationAdminResponse;
import com.santiagomac.invitation.application.dto.InvitationStatsResponse;
import com.santiagomac.invitation.application.ports.out.InvitationGateway;
import com.santiagomac.invitation.domain.model.invitation.InvitationModel;
import com.santiagomac.invitation.domain.model.invitation.InvitationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListInvitationsUseCase {

    private final InvitationGateway invitationGateway;

    public List<InvitationAdminResponse> listAll() {
        return invitationGateway.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public InvitationStatsResponse getStats() {
        List<InvitationModel> invitations = invitationGateway.findAll();

        long confirmedInvitations = invitations.stream().filter(i -> i.getStatus() == InvitationStatus.CONFIRMED).count();
        long declinedInvitations = invitations.stream().filter(i -> i.getStatus() == InvitationStatus.DECLINED).count();
        long pendingInvitations = invitations.stream().filter(i -> i.getStatus() == InvitationStatus.PENDING).count();
        long totalInvitedGuests = invitations.stream().mapToLong(InvitationModel::getMaxGuests).sum();
        long confirmedGuests = invitations.stream()
                .filter(i -> i.getStatus() == InvitationStatus.CONFIRMED)
                .mapToLong(i -> i.getConfirmedGuests() == null ? 0 : i.getConfirmedGuests())
                .sum();

        return InvitationStatsResponse.builder()
                .totalInvitations(invitations.size())
                .totalInvitedGuests(totalInvitedGuests)
                .pendingInvitations(pendingInvitations)
                .confirmedInvitations(confirmedInvitations)
                .declinedInvitations(declinedInvitations)
                .confirmedGuests(confirmedGuests)
                .build();
    }

    private InvitationAdminResponse toResponse(InvitationModel invitation) {
        return InvitationAdminResponse.builder()
                .id(invitation.getId())
                .code(invitation.getCode())
                .displayName(invitation.getDisplayName())
                .maxGuests(invitation.getMaxGuests())
                .status(invitation.getStatus())
                .confirmedGuests(invitation.getConfirmedGuests())
                .respondedAt(invitation.getRespondedAt())
                .createdAt(invitation.getCreatedAt())
                .build();
    }
}
