package com.example.couponserver.coupon.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(404,"M-4001","해당 가게는 존재하지 않습니다."),
    //event error(4101 ~ 4200)
    PROMOTION_NOT_FOUND(404, "4101", "해당 이벤트를 찾을 수 없습니다."),
    PROMOTION_NOT_ACTIVE(400, "4102", "이벤트 기간이 아닙니다."),

    // coupon error (4201 ~ 4250)
    COUPON_NOT_FOUND(404, "4201", "해당 쿠폰을 찾을 수 없습니다."),
    COUPON_NOT_ACTIVE(400, "4202", "쿠폰 발급 기간이 아닙니다."),
    COUPON_OUT_OF_STOCK(409, "4203", "쿠폰 재고가 부족합니다."),
    COUPON_ALREADY_ISSUED(409, "4204", "이미 쿠폰을 발급받았습니다."),

    // coupon issue error (4251 ~ 4300)
    COUPON_ALREADY_USED(409, "4251", "쿠폰을 이미 사용했습니다."),
    COUPON_EXPIRED(400, "4252", "쿠폰 사용기간이 아닙니다.");


    private final Integer httpStatus;
    private final String code;
    private final String message;
}
