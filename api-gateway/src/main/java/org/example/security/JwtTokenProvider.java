package org.example.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class JwtTokenProvider {

    private final String secretKey;

    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "roles";

    public JwtTokenProvider(String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
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
