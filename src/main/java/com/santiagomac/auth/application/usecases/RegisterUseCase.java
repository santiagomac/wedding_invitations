package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.RegisterRequest;
import com.santiagomac.auth.application.dto.RegisterResponse;
import com.santiagomac.auth.application.ports.out.PasswordPort;
import com.santiagomac.auth.application.ports.out.UserGateway;
import com.santiagomac.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUseCase {

    private final PasswordPort passwordPort;
    private final UserGateway userGateway;

    public RegisterResponse signup(RegisterRequest registerRequest) {
        var passwordEncrypted = this.passwordPort.encryptPassword(registerRequest.getPassword());
        UserModel newUser = UserModel.createNewUser(registerRequest.getEmail(), passwordEncrypted, registerRequest.getRoleId());

        var userCreated = this.userGateway.save(newUser);
        return RegisterResponse
                .builder()
                .id(userCreated.getId())
                .email(userCreated.getEmail())
                .build();
    }
}
