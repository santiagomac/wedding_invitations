package com.santiagomac.invitation.domain.model.invitation;

import com.santiagomac.invitation.domain.model.exceptions.InvalidGuestCountException;
import com.santiagomac.invitation.domain.model.exceptions.InvalidInvitationException;
import com.santiagomac.invitation.domain.model.exceptions.InvalidRsvpTransitionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InvitationModel {

    private UUID id;
    private String code;
    private String displayName;
    private int maxGuests;
    private InvitationStatus status;
    private Integer confirmedGuests;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InvitationModel createNew(String code, String displayName, int maxGuests) {
        validDisplayName(displayName);
        validMaxGuests(maxGuests);

        return InvitationModel.builder()
                .code(code)
                .displayName(displayName)
                .maxGuests(maxGuests)
                .status(InvitationStatus.PENDING)
                .build();
    }

    public void confirm(Integer confirmedGuests) {
        if (this.status == InvitationStatus.DECLINED) {
            throw new InvalidRsvpTransitionException("This invitation was already declined and cannot be confirmed");
        }
        if (confirmedGuests == null || confirmedGuests < 1 || confirmedGuests > this.maxGuests) {
            throw new InvalidGuestCountException("confirmedGuests must be between 1 and " + this.maxGuests);
        }

        this.status = InvitationStatus.CONFIRMED;
        this.confirmedGuests = confirmedGuests;
        this.respondedAt = LocalDateTime.now();
    }

    public void decline() {
        this.status = InvitationStatus.DECLINED;
        this.confirmedGuests = 0;
        this.respondedAt = LocalDateTime.now();
    }

    private static void validDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new InvalidInvitationException("displayName must not be blank");
        }
    }

    private static void validMaxGuests(int maxGuests) {
        if (maxGuests < 1) {
            throw new InvalidInvitationException("maxGuests must be at least 1");
        }
    }
}
