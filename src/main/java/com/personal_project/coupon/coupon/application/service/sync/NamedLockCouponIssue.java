package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.application.usecase.CouponIssueFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NamedLockCouponIssue implements CouponIssueFacade {
    private final CouponOutPort couponOutPort;
    private final CouponIssueService couponIssueService;

    @Override
    public void issueCoupon(Long eventId, Long couponId, Long memberId) {
        String lockKey = "coupon_" + couponId;

        try {
            log.info("Acquiring Named Lock: {}", lockKey);
            couponOutPort.getLock(lockKey); // 네임드 락 획득

            // 기본 쿠폰 발급 로직 실행
            couponIssueService.issueCoupon(eventId, couponId, memberId);

        } finally {
            log.info("Releasing Named Lock: {}", lockKey);
            couponOutPort.releaseLock(lockKey); // 네임드 락 해제
        }
    }
}
