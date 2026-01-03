package org.example.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 인증 처리
    JWT_EMPTY(401, "JWT4100", "JWT 토큰을 넣어주세요."),
    JWT_INVALID(400, "JWT4101", "다시 로그인 해주세요.(토큰이 유효하지 않습니다.)"),
    JWT_EXPIRED(401, "JWT4102", "토큰이 만료되었습니다."),
    JWT_BAD(400, "JWT4103", "JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(409, "JWT4104", "RefreshToken이 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "JWT4105", "리프레시 토큰을 찾을 수 없습니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
