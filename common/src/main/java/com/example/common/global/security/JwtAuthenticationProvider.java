package com.example.common.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    public Authentication authenticate(String token) {
        Long userId = jwtTokenProvider.getId(token);
        String role = jwtTokenProvider.getRole(token);

        AuthPrincipal principal =
                new AuthPrincipal(userId, role);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}