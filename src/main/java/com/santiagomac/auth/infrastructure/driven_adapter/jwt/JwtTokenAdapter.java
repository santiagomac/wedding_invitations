package com.santiagomac.auth.infrastructure.driven_adapter.jwt;

import com.santiagomac.auth.application.ports.out.JwtTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenPort {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Override
    public String generateAccessToken(String subject, String roles) {
        return generateToken(subject, roles, 15, ChronoUnit.MINUTES);
    }

    @Override
    public String generateRefreshToken(String subject, String roles) {
        return generateToken(subject, roles, 7, ChronoUnit.DAYS);
    }

    private String generateToken(String subject, String roles, long amount, ChronoUnit unit) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth_service")
                .issuedAt(now)
                .expiresAt(now.plus(amount, unit))
                .subject(subject)
                .claim("roles", roles)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(Instant.now());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getSubjectFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaims().get("sub").toString();
    }
}
