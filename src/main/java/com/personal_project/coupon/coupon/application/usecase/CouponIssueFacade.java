package com.personal_project.coupon.coupon.application.usecase;

public interface CouponIssueFacade {
    void issueCoupon(Long eventId, Long couponId, Long memberId);
}
