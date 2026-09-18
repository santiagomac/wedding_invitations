package com.santiagomac.auth.infrastructure.driven_adapter.jpa.token;

import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.application.ports.out.SessionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionGateway {
    private final SessionRepository sessionRepository;

    @Override
    public Session createSession(Session session) {
        SessionEntity entity = toEntity(session);
        return toModel(sessionRepository.save(entity));
    }

    @Override
    public Optional<Session> findByRefreshToken(String refreshToken) {
        return this.sessionRepository.findByRefreshToken(refreshToken)
                .map(this::toModel);
    }

    @Override
    public void updateSession(Session session) {
        SessionEntity entity = toEntity(session);
        this.sessionRepository.save(entity);
    }

    private SessionEntity toEntity(Session session) {
        return SessionEntity.builder()
                .id(Objects.isNull(session.getId()) ? null : session.getId())
                .refreshToken(session.getRefreshToken())
                .accessToken(session.getAccessToken())
                .userId(session.getUserId())
                .build();
    }

    private Session toModel(SessionEntity entity) {
        return Session.builder()
                .id(entity.getId())
                .refreshToken(entity.getRefreshToken())
                .userId(entity.getUserId())
                .accessToken(entity.getAccessToken())
                .build();
    }
}
