package com.example.couponserver.coupon.application.usecase;

import com.example.couponserver.coupon.domain.model.event.EventType;

public interface AddCouponIssueLogUseCase {
    void addCouponIssue(Long memberId, Long couponId, EventType type);
    void flush();
}
