package com.santiagomac.invitation.infrastructure.driven_adapter.code;

import com.santiagomac.invitation.application.ports.out.CodeGeneratorPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomCodeGeneratorAdapter implements CodeGeneratorPort {

    // Unambiguous alphabet: no 0/O, 1/I/L, to keep codes easy to read and type from an invite card.
    private static final String ALPHABET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }
}
