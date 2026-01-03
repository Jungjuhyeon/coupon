package com.example.couponserver.coupon.domain.model.event;

public enum EventType {
    SUCCESS,            // 정상 발급
    DUPLICATE,          // 이미 발급된 경우
    OUT_OF_STOCK,       // 재고 부족
    INVALID_TIME        // 이벤트 시간 아님
}
