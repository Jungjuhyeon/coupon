package com.personal_project.coupon.coupon.application.usecase;

public interface IssueCouponUsecase {

    void issue(Long eventId, Long couponId, Long memberId);
}
