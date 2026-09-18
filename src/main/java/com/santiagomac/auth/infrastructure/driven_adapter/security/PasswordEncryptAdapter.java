package com.santiagomac.auth.infrastructure.driven_adapter.security;

import com.santiagomac.auth.application.ports.out.PasswordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class PasswordEncryptAdapter implements PasswordPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encryptPassword(String password) {
        return this.passwordEncoder.encode(password);
    }
}
