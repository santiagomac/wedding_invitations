package com.santiagomac.auth.domain.model.user;

import com.santiagomac.auth.domain.model.exceptions.NotFoundException;

public enum RoleEnum {
    ADMIN,
    PROFESSIONAL;


    public static RoleEnum getRoleByName(String name) {
        for (RoleEnum role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }

        throw new NotFoundException("Role not found: " + name);
    }
}
