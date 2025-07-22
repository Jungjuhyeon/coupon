package com.personal_project.coupon.coupon.application.usercase;

public interface IssueCoupon {

    void issue(Long eventId, Long couponId, Long memberId);
}
