package com.santiagomac.invitation.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InvitationStatsResponse {

    private long totalInvitations;
    private long totalInvitedGuests;
    private long pendingInvitations;
    private long confirmedInvitations;
    private long declinedInvitations;
    private long confirmedGuests;
}
