package com.example.orderserver.order.auth;

import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.common.global.exception.response.ApiResponse;
import com.example.common.global.exception.response.ErrorResponse;
import com.example.common.global.security.AuthPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-USER-ID");
        String role = request.getHeader("X-USER-ROLE");

        if (userId != null && role != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            AuthPrincipal principal = new AuthPrincipal(Long.valueOf(userId), role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal, null, List.of(new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

    public static void setErrorResponse(HttpServletResponse response, CommonErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus());
        ObjectMapper objectMapper = new ObjectMapper();

        ApiResponse apiResponse = ErrorResponse.of(errorCode);
        String s = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(s);
    }
}
