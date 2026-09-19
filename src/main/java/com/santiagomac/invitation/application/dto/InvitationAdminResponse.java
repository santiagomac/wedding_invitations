package com.santiagomac.invitation.application.dto;

import com.santiagomac.invitation.domain.model.invitation.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InvitationAdminResponse {

    private UUID id;
    private String code;
    private String displayName;
    private int maxGuests;
    private InvitationStatus status;
    private Integer confirmedGuests;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
