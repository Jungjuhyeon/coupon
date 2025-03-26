package com.personal_project.coupon.coupon.application.service;

import com.personal_project.coupon.coupon.application.usercase.CouponIssueFacade;
import com.personal_project.coupon.global.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockCouponIssue implements CouponIssueFacade {

    private final DefaultCouponIssue defaultCouponIssue;

    @Override
    @DistributedLock(value = "couponId:#couponId")
    public void issueCoupon(Long eventId, Long couponId, Long memberId) {
        defaultCouponIssue.issueCoupon(eventId, couponId, memberId);
    }

}
