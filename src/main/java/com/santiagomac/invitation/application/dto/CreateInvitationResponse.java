package com.santiagomac.invitation.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateInvitationResponse {

    private UUID id;
    private String code;
    private String displayName;
    private int maxGuests;
}
