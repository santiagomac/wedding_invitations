package com.santiagomac.auth.domain.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private UUID id;
    private String email;
    private String password;
    private boolean enabled;
    private UUID roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserModel(UUID id, String email, String password, boolean enabled, UUID roleId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserModel(String email, String password, UUID roleId) {
        this.roleId = roleId;
        this.email = email;
        this.password = password;
        this.enabled = true;
    }

    public static UserModel createNewUser(String email, String password, UUID roleId) {
        validEmail(email);
        validPassword(password);

        return new UserModel(email, password, roleId);
    }

    private static void validEmail(String email) {
        var pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    private static void validPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }

}
