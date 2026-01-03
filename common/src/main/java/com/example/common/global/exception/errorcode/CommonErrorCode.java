package com.example.common.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // 공용 처리
    INVALID_PARAMETER(400, "4000", "Invalid parameter included"),
    RESOURCE_NOT_FOUND(404, "4040", "Resource not exists"),
    INTERNAL_SERVER_ERROR(500, "5000", "알수없는 에러 관리자에게 문의"),

    JWT_AUTHORIZATION_FAILED(401,"JWT4105","권한이 없습니다."),


    REDIS_SCRIPT_ERROR(500, "5001", "Redis 스크립트 실행 중 오류가 발생했습니다."),
    LOCK_ACQUISITION_FAILED(500, "5002", "분산 락 획득에 실패했습니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
