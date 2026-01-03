package com.example.memberserver.member.exception;

import com.example.common.global.exception.errorcode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    //user error (4001~
    DUPLICATE_EMAIL(409,"4000","이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(404,"4001","해당 유저를 찾을 수 없습니다."),
    USER_EMAIL_NOT_FOUND(404, "4002", "일치하는 이메일이 없습니다."),
    USER_PASSWORD_MISMATCH(401, "4003", "비밀번호가 일치하지 않습니다."),
    LOGOUT_MEMBER(403, "3001", "로그아웃된 사용자입니다.(재 로그인 하세요.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
