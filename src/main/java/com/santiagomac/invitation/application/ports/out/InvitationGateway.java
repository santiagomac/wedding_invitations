package com.santiagomac.invitation.application.ports.out;

import com.santiagomac.invitation.domain.model.invitation.InvitationModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationGateway {

    Optional<InvitationModel> findByCode(String code);

    Optional<InvitationModel> findById(UUID id);

    boolean existsByCode(String code);

    InvitationModel save(InvitationModel invitation);

    List<InvitationModel> saveAll(List<InvitationModel> invitations);

    List<InvitationModel> findAll();
}
