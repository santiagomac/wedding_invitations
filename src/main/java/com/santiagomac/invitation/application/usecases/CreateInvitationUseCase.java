package com.santiagomac.invitation.application.usecases;

import com.santiagomac.invitation.application.dto.BulkCreateInvitationRequest;
import com.santiagomac.invitation.application.dto.CreateInvitationRequest;
import com.santiagomac.invitation.application.dto.CreateInvitationResponse;
import com.santiagomac.invitation.application.ports.out.CodeGeneratorPort;
import com.santiagomac.invitation.application.ports.out.InvitationGateway;
import com.santiagomac.invitation.domain.model.exceptions.DuplicateInvitationCodeException;
import com.santiagomac.invitation.domain.model.invitation.InvitationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateInvitationUseCase {

    private static final int MAX_CODE_ATTEMPTS = 10;

    private final InvitationGateway invitationGateway;
    private final CodeGeneratorPort codeGeneratorPort;

    public CreateInvitationResponse create(CreateInvitationRequest request) {
        InvitationModel created = invitationGateway.save(buildInvitation(request));
        return toResponse(created);
    }

    public List<CreateInvitationResponse> createBulk(BulkCreateInvitationRequest request) {
        List<InvitationModel> invitations = request.getInvitations().stream()
                .map(this::buildInvitation)
                .toList();

        return invitationGateway.saveAll(invitations).stream()
                .map(this::toResponse)
                .toList();
    }

    private InvitationModel buildInvitation(CreateInvitationRequest request) {
        String code = generateUniqueCode();
        return InvitationModel.createNew(code, request.getDisplayName(), request.getMaxGuests());
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < MAX_CODE_ATTEMPTS; attempt++) {
            String candidate = codeGeneratorPort.generateCode();
            if (!invitationGateway.existsByCode(candidate)) {
                return candidate;
            }
        }
        throw new DuplicateInvitationCodeException("Could not generate a unique invitation code after " + MAX_CODE_ATTEMPTS + " attempts");
    }

    private CreateInvitationResponse toResponse(InvitationModel invitation) {
        return CreateInvitationResponse.builder()
                .id(invitation.getId())
                .code(invitation.getCode())
                .displayName(invitation.getDisplayName())
                .maxGuests(invitation.getMaxGuests())
                .build();
    }
}
