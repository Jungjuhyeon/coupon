package com.personal_project.coupon.coupon.application.usercase;

public interface CouponIssueFacade {
    void issueCoupon(Long eventId, Long couponId, Long memberId);
}
