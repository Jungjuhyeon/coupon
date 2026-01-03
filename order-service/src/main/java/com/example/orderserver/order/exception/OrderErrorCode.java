package com.example.orderserver.order.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(404, "4601", "해당 주문는 존재하지 않습니다."),
    ORDER_PRECONDITION_FAILED(412, "4602", "주문을 처리할 수 없는 상태입니다."),
    ORDER_VIEW_RESOURCE_NOT_FOUND(404, "4603", "주문 정보를 조회할 수 없습니다. 연관된 가게 또는 메뉴 정보가 존재하지 않습니다."),
    // coupon apply error (4701 ~)
    COUPON_NOT_APPLICABLE(400, "4703", "해당 쿠폰은 적용할 수 없습니다.");
    private final Integer httpStatus;
    private final String code;
    private final String message;
}
