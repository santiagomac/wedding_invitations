package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.application.ports.out.JwtTokenPort;
import com.santiagomac.auth.domain.model.exceptions.InvalidRefreshToken;
import com.santiagomac.auth.domain.model.exceptions.NotFoundException;
import com.santiagomac.auth.domain.model.exceptions.RefreshTokenNotFound;
import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.application.ports.out.RoleGateway;
import com.santiagomac.auth.application.ports.out.SessionGateway;
import com.santiagomac.auth.application.ports.out.UserGateway;
import com.santiagomac.auth.domain.model.user.RoleEnum;
import com.santiagomac.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUseCase {

    private final SessionGateway sessionGateway;
    private final JwtTokenPort jwtTokenPort;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public AuthResponse refreshToken(String refreshToken) {
        Optional<Session> optionalSession = this.sessionGateway.findByRefreshToken(refreshToken);
        if (optionalSession.isEmpty()) {
            throw new RefreshTokenNotFound("The provided refresh token not exists");
        }

        Session session = optionalSession.get();
        if (!jwtTokenPort.isValidToken(session.getRefreshToken())) {
            throw new InvalidRefreshToken("The refresh token is invalid");
        }

        String email = jwtTokenPort.getSubjectFromToken(session.getRefreshToken());

        UserModel user = userGateway.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RoleEnum role = roleGateway.getRole(user.getRoleId());

        String accessToken = jwtTokenPort.generateAccessToken(email, "ROLE_" + role.name());

        session.setAccessToken(accessToken);
        sessionGateway.updateSession(session);

        return AuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
