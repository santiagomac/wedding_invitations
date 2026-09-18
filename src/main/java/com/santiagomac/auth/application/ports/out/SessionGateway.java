package com.santiagomac.auth.application.ports.out;

import com.santiagomac.auth.domain.model.session.Session;

import java.util.Optional;

public interface SessionGateway {

    Session createSession(Session session);

    Optional<Session> findByRefreshToken(String refreshToken);

    void updateSession(Session session);
}
