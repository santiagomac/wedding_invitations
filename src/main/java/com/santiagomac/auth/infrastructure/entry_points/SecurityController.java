package com.santiagomac.auth.infrastructure.entry_points;

import com.santiagomac.auth.application.dto.AuthRequest;
import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.application.dto.RegisterRequest;
import com.santiagomac.auth.application.dto.RegisterResponse;
import com.santiagomac.auth.application.usecases.AuthUseCase;
import com.santiagomac.auth.application.usecases.LoginUseCase;
import com.santiagomac.auth.application.usecases.RegisterUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final AuthUseCase authUseCase;

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        var registerUser = this.registerUseCase.signup(request);

        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        var session = this.loginUseCase.authenticate(authRequest);
        return setCookies(response, session);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Void> refreshToken(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
        AuthResponse session = this.authUseCase.refreshToken(refreshToken);

        return setCookies(response, session);
    }

    @NonNull
    private ResponseEntity<Void> setCookies(HttpServletResponse response, AuthResponse session) {
        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", session.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", session.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().build();
    }
}
