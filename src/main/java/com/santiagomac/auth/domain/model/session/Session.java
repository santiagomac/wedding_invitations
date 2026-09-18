package com.santiagomac.auth.domain.model.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class Session {
    private UUID id;
    private String accessToken;
    private String refreshToken;
    private UUID userId;
}