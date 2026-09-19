package com.santiagomac.invitation.infrastructure.driven_adapter.jpa.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {

    Optional<InvitationEntity> findByCode(String code);

    boolean existsByCode(String code);
}
