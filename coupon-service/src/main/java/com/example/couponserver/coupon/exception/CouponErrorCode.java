package com.example.couponserver.coupon.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {
    //event error(4101 ~ 4200)
    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND,"4101","해당 이벤트를 찾을 수 없습니다."),
    PROMOTION_NOT_ACTIVE(HttpStatus.NOT_FOUND,"4102","이벤트 기간이 아닙니다."),

    //coupon error(4201 ~ 4250)
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND,"4201","해당 쿠폰를 찾을 수 없습니다."),
    COUPON_NOT_ACTIVE(HttpStatus.NOT_FOUND,"4202","쿠폰 발급 기간이 아닙니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.NOT_FOUND,"4203", "쿠폰 재고가 부족합니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "4204", "이미 쿠폰을 발급받았습니다."),

    //coupon issue error(4251 ~ 4300)
    COUPON_ALREADY_USED(HttpStatus.BAD_REQUEST, "4251", "쿠폰을 이미 사용했습니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "4252", "쿠폰 사용기간이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
