package com.santiagomac.auth.infrastructure.driven_adapter.security;

import com.santiagomac.auth.domain.model.user.RoleEnum;
import com.santiagomac.auth.application.ports.out.RoleGateway;
import com.santiagomac.auth.application.ports.out.UserGateway;
import com.santiagomac.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {


    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = this.userGateway.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        RoleEnum role = roleGateway.getRole(user.getRoleId());


        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(role.name())
                .disabled(!user.isEnabled())
                .build();
    }
}
