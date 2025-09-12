package com.personal_project.coupon.coupon.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IssueCouponUsecase {

    void issue(Long promotionId, Long couponId, Long memberId) throws JsonProcessingException;
}
