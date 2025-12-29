package com.example.common.global.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "roles";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public void validate(String token) {
        JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
    }

    public Long getId(String token) {
        return decode(token).getClaim(ID_CLAIM).asLong();
    }
    public String getRole(String token) {
        return decode(token).getClaim(ROLE_CLAIM).asString();
    }
    public Long getExpiration(String token) {
        DecodedJWT decodedJWT = decode(token);
        return decodedJWT.getExpiresAt().getTime() - System.currentTimeMillis();
    }

    private DecodedJWT decode(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
