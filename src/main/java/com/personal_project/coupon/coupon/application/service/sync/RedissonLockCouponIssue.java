package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.usecase.CouponIssueFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockCouponIssue implements CouponIssueFacade {

    private final RedissonCouponIssueService redissonCouponIssueService;

    @Override
    public void issueCoupon(Long eventId, Long couponId, Long memberId) {
        redissonCouponIssueService.issueCoupon(eventId, couponId, memberId);
    }

}
