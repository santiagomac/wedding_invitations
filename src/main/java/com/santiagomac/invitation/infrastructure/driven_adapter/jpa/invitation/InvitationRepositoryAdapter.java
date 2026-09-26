package com.santiagomac.invitation.infrastructure.driven_adapter.jpa.invitation;

import com.santiagomac.invitation.application.ports.out.InvitationGateway;
import com.santiagomac.invitation.domain.model.invitation.InvitationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InvitationRepositoryAdapter implements InvitationGateway {

    private final InvitationRepository invitationRepository;

    @Override
    public Optional<InvitationModel> findByCode(String code) {
        return this.invitationRepository.findByCode(code)
                .map(this::toModel);
    }

    @Override
    public Optional<InvitationModel> findById(UUID id) {
        return this.invitationRepository.findById(id)
                .map(this::toModel);
    }

    @Override
    public boolean existsByCode(String code) {
        return this.invitationRepository.existsByCode(code);
    }

    @Override
    public InvitationModel save(InvitationModel invitation) {
        InvitationEntity savedEntity = this.invitationRepository.save(toEntity(invitation));
        return this.toModel(savedEntity);
    }

    @Override
    public List<InvitationModel> saveAll(List<InvitationModel> invitations) {
        List<InvitationEntity> entities = invitations.stream()
                .map(this::toEntity)
                .toList();

        return this.invitationRepository.saveAll(entities).stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public List<InvitationModel> findAll() {
        return this.invitationRepository.findAll().stream()
                .map(this::toModel)
                .toList();
    }

    private InvitationEntity toEntity(InvitationModel model) {
        return InvitationEntity.builder()
                .id(model.getId())
                .code(model.getCode())
                .displayName(model.getDisplayName())
                .maxGuests(model.getMaxGuests())
                .status(model.getStatus())
                .confirmedGuests(model.getConfirmedGuests())
                .createdAt(model.getCreatedAt())
                .respondedAt(model.getRespondedAt())
                .build();
    }

    private InvitationModel toModel(InvitationEntity entity) {
        return InvitationModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .displayName(entity.getDisplayName())
                .maxGuests(entity.getMaxGuests())
                .status(entity.getStatus())
                .confirmedGuests(entity.getConfirmedGuests())
                .respondedAt(entity.getRespondedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
