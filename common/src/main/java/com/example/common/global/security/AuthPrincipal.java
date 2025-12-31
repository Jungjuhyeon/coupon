package com.example.common.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthPrincipal  {

    private Long id;
    private String role;
}
