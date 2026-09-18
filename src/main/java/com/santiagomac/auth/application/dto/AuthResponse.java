package com.santiagomac.auth.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
