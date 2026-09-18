package com.santiagomac.auth.application.ports.out;

import com.santiagomac.auth.domain.model.user.RoleEnum;

import java.util.UUID;

public interface RoleGateway {

    RoleEnum getRole(UUID id);
}
