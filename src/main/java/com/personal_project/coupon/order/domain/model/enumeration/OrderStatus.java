package com.personal_project.coupon.order.domain.model.enumeration;

public enum OrderStatus {
    PENDING,     // 주문 대기
    ACCEPTED,    // 가게 수락
    IN_PROGRESS, // 조리 중
    READY,       // 준비 완료
    COMPLETED,   // 완료
    CANCELLED,   // 취소
    FAILED,      // 실패
    REFUNDED     // 환불됨
}
