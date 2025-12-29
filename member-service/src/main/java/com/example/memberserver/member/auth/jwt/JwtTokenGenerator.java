package com.example.memberserver.member.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenGenerator {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // 토큰 유효시간 30분
    public static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60;
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144;
    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "roles";
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(Long id, String role) {
        return JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
                .withIssuedAt(new Date())
                .withClaim(ID_CLAIM, id)
                .withClaim(ROLE_CLAIM, role)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken(Long id) {
        return JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                .withIssuedAt(new Date())
                .withClaim(ID_CLAIM, id)
                .sign(Algorithm.HMAC512(secretKey));
    }


}
