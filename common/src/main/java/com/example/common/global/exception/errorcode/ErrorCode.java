package com.example.common.global.exception.errorcode;

public interface ErrorCode {
    Integer getHttpStatus();
    String getCode();
    String getMessage();
}
