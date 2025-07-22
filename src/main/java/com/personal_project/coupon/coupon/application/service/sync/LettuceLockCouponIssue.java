package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.usecase.CouponIssueFacade;
import com.personal_project.coupon.global.infrastructure.redislock.RedisLockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LettuceLockCouponIssue implements CouponIssueFacade {

    private final CouponIssueService couponIssueService;
    private final RedisLockManager redisLockManager;
    private static final String LOCK_PREFIX = "LOCK:COUPON:"; // 락을 위한 키 접두어

    @Override
    public void issueCoupon(Long eventId, Long couponId, Long memberId) {
        String lockKey = LOCK_PREFIX + couponId; // 쿠폰 ID를 락 키로 사용
        String lockValue = redisLockManager.lock(lockKey);

        if (lockValue == null) {
            throw new RuntimeException("쿠폰 발급 중 중복 요청이 발생했습니다. 잠시 후 다시 시도해주세요.");
        }

        try {
            couponIssueService.issueCoupon(eventId, couponId, memberId);
        } finally {
            redisLockManager.unlock(lockKey, lockValue);
        }
    }

}
