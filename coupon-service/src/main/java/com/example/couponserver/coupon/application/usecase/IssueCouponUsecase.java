package com.example.couponserver.coupon.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IssueCouponUsecase {
    void issue(Long promotionId, Long couponId, Long memberId) throws JsonProcessingException;
}
