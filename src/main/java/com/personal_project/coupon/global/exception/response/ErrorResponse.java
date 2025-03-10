package com.personal_project.coupon.global.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal_project.coupon.global.exception.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse extends ApiResponse{

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors = new ArrayList<>();

    private ErrorResponse(ErrorCode errorCode){
        super(false, errorCode.getCode(), errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode);
    }

    private ErrorResponse(ErrorCode errorCode, String message){
        super(false, errorCode.getCode(), message);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message){
        return new ErrorResponse(errorCode, message);
    }


    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"isSuccess\":" + super.getIsSuccess() + ",\n" +
                "    \"code\":\"" + super.getCode() + "\",\n" +
                "    \"message\":\"" + super.getMessage() + "\"\n" +
                "}";
    }
}
