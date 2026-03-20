package com.example.orderserver.order.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(404, "4001", "해당 회원은 존재하지 않습니다."),
    MEMBER_SERVICE_UNAVAILABLE(503, "4002", "회원 서비스가 일시적으로 이용 불가능합니다. 잠시 후 다시 시도해주세요."),

    ORDER_NOT_FOUND(404, "4601", "해당 주문는 존재하지 않습니다."),
    ORDER_PRECONDITION_FAILED(412, "4602", "주문을 처리할 수 없는 상태입니다."),
    ORDER_VIEW_RESOURCE_NOT_FOUND(404, "4603", "주문 정보를 조회할 수 없습니다. 연관된 가게 또는 메뉴 정보가 존재하지 않습니다."),
    // coupon apply error (4701 ~)
    COUPON_NOT_FOUND(404, "4701", "해당 쿠폰은 존재하지 않습니다."),
    COUPON_EXPIRED(400, "4702", "쿠폰 사용기간이 아닙니다."),
    COUPON_ALREADY_USED(409, "4703", "쿠폰을 이미 사용했습니다."),
    COUPON_NOT_APPLICABLE(400, "4704", "해당 쿠폰은 적용할 수 없습니다."),
    COUPON_SERVICE_UNAVAILABLE(503, "4705", "쿠폰 서비스가 일시적으로 이용 불가능합니다. 잠시 후 다시 시도해주세요."),

    STORE_NOT_FOUND(404, "4801", "해당 가게는 존재하지 않습니다."),
    STORE_SERVICE_UNAVAILABLE(503, "4802", "가게 서비스가 일시적으로 이용 불가능합니다. 잠시 후 다시 시도해주세요."),

    MENU_NOT_FOUND(404, "4901", "해당 가게 메뉴는 존재하지 않습니다.");


    private final Integer httpStatus;
    private final String code;
    private final String message;
}
