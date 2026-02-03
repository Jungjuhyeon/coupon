package com.example.couponserver.coupon.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IssueCouponUseCase {
    void issue(Long promotionId, Long couponId, Long memberId) throws JsonProcessingException;
}
