package org.example.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.common.global.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.exception.AuthErrorCode;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> WHITE_LIST = List.of(
            "/api/v1/members/login",
            "/api/v1/members/signup"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (isWhiteListed(path)) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return error(exchange, AuthErrorCode.JWT_EMPTY);
        }

        String token = authHeader.substring(7);

        try {
            jwtTokenProvider.validate(token);
            Long userId = jwtTokenProvider.getId(token);
            String role = jwtTokenProvider.getRole(token);

            ServerHttpRequest mutated =
                    request.mutate()
                            .header("X-USER-ID", String.valueOf(userId))
                            .header("X-USER-ROLE", role)
                            .build();
            return chain.filter(exchange.mutate().request(mutated).build());

        } catch (TokenExpiredException e) {
            return error(exchange, AuthErrorCode.JWT_EXPIRED);
        } catch (JWTVerificationException e) {
            return error(exchange, AuthErrorCode.JWT_BAD);
        }
    }

    private boolean isWhiteListed(String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        return WHITE_LIST.stream().anyMatch(p -> matcher.match(p, path));
    }

    private Mono<Void> error(ServerWebExchange exchange, AuthErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(errorCode.getHttpStatus()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse body = ErrorResponse.of(errorCode);
        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(body);
        } catch (Exception e) {
            bytes = new byte[0];
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

