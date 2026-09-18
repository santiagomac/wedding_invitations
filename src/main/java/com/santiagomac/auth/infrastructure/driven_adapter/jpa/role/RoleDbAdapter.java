package com.santiagomac.auth.infrastructure.driven_adapter.jpa.role;

import com.santiagomac.auth.domain.model.exceptions.NotFoundException;
import com.santiagomac.auth.domain.model.user.RoleEnum;
import com.santiagomac.auth.application.ports.out.RoleGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleDbAdapter implements RoleGateway {

    private final RoleRepository roleRepository;


    @Override
    public RoleEnum getRole(UUID id) {
        return this.roleRepository.findById(id)
                .map(role -> RoleEnum.getRoleByName(role.getName()))
                .orElseThrow(() -> new NotFoundException("Role not found!"));
    }
}
