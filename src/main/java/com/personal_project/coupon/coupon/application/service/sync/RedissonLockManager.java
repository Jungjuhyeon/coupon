package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonLockManager {

    private final RedissonClient redissonClient;
    private final CouponIssueServiceHelper couponIssueServiceHelper;

    public void saveIssuedCouponWithLock(Long couponId, Long memberId, LocalDateTime now) {
        RLock lock = redissonClient.getLock("couponId:" + couponId);
        try {
            boolean isLocked = lock.tryLock(5, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(CommonErrorCode.LOCK_ACQUISITION_FAILED);
            }

            // 이 시점에만 트랜잭션 진입
            couponIssueServiceHelper.saveIssuedCoupon(couponId, memberId, now);

        } catch (InterruptedException e) {
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 트랜잭션 끝나고 나서 해제
            }
        }
    }
}
