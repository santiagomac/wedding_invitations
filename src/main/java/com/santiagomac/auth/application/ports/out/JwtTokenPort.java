package com.santiagomac.auth.application.ports.out;

public interface JwtTokenPort {
    String generateAccessToken(String subject, String roles);
    String generateRefreshToken(String subject, String roles);
    boolean isValidToken(String token);
    String getSubjectFromToken(String token);
}
