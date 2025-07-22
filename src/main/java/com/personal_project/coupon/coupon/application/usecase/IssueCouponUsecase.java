package com.personal_project.coupon.coupon.application.usecase;

public interface IssueCouponUsecase {

    void issue(Long promotionId, Long couponId, Long memberId);
}
