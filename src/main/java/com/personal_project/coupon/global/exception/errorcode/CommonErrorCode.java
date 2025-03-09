package com.personal_project.coupon.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // 공용 처리
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4000", "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "4040", "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "알수없는 에러 관리자에게 문의"),

    // 인증 처리
    JWT_EMPTY(HttpStatus.NO_CONTENT,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.BAD_REQUEST,"JWT4101","다시 로그인 해주세요.(토큰이 유효하지 않습니다.)"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT4102","토큰이 만료되었습니다."),
    JWT_BAD(HttpStatus.BAD_REQUEST,"JWT4103","JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(HttpStatus.CONFLICT,"JWT4104","RefreshToken이 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "4104", "리프레시 토큰을 찾을 수 없습니다."),

    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED,"JWT4105","권한이 없습니다."),

    //user error (4001~
    DUPLICATE_EMAIL(HttpStatus.CONFLICT,"4000","이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"4001","해당 유저를 찾을 수 없습니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "4002", "일치하는 이메일이 없습니다."),
    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "4003", "비밀번호가 일치하지 않습니다."),
    LOGOUT_MEMBER(HttpStatus.FORBIDDEN, "3001", "로그아웃된 사용자입니다.(재 로그인 하세요."),


    //event error(4101 ~ 4200)
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND,"4101","해당 이벤트를 찾을 수 없습니다."),
    EVENT_NOT_ACTIVE(HttpStatus.NOT_FOUND,"4102","이벤트 기간이 아닙니다."),

    //coupon error(4201 ~ 4300)
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND,"4201","해당 쿠폰를 찾을 수 없습니다."),
    COUPON_NOT_ACTIVE(HttpStatus.NOT_FOUND,"4202","쿠폰 발급 기간이 아닙니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.NOT_FOUND,"4203", "쿠폰 재고가 부족합니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "4204", "이미 쿠폰을 발급받았습니다.");
    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
