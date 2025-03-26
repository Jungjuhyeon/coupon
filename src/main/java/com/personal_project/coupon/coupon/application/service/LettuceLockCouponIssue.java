package com.personal_project.coupon.coupon.application.service;

import com.personal_project.coupon.coupon.application.usercase.CouponIssueFacade;
import com.personal_project.coupon.global.infrastructure.redis.RedisLockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LettuceLockCouponIssue implements CouponIssueFacade {

    private final DefaultCouponIssue defaultCouponIssue;
    private final RedisLockManager redisLockManager;
    private static final String LOCK_PREFIX = "LOCK:COUPON:"; // 락을 위한 키 접두어

    @Override
    public void issueCoupon(Long eventId, Long couponId, Long memberId) {
        String lockKey = LOCK_PREFIX + couponId; // 쿠폰 ID를 락 키로 사용

        Boolean lockAcquired = redisLockManager.lock(lockKey); // 락 시도
        if (!lockAcquired) {
            throw new RuntimeException("쿠폰 발급 중 중복 요청이 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        try {
            defaultCouponIssue.issueCoupon(eventId, couponId, memberId);
        } finally {
            redisLockManager.unlock(lockKey); // 작업 완료 후 락 해제
        }
    }

}
