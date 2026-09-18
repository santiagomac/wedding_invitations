package com.santiagomac.auth.infrastructure.driven_adapter.jpa.user;

import com.santiagomac.auth.domain.model.user.UserModel;
import com.santiagomac.auth.application.ports.out.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserGateway {

    private final UserRepository userRepository;


    @Override
    public Optional<UserModel> findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(this::toModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity userEntity = this.toEntity(userModel);
        UserEntity savedEntity = this.userRepository.save(userEntity);
        return this.toModel(savedEntity);
    }

    private UserEntity toEntity(UserModel model) {
        return UserEntity.builder()
                .email(model.getEmail())
                .password(model.getPassword())
                .roleId(model.getRoleId())
                .build();
    }

    private UserModel toModel(UserEntity entity) {
        return UserModel.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .enabled(entity.isEnabled())
                .roleId(entity.getRoleId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
