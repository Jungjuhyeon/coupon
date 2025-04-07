package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.outputport.*;
import com.personal_project.coupon.coupon.domain.CouponCache;
import com.personal_project.coupon.coupon.domain.EventCache;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonCouponIssueService {

    private final EventCacheOutPort eventCacheOutPort;
    private final CouponCacheOutPort couponCacheOutPort;
    private final RedissonLockManager redissonLockManager;

    public void issueCoupon(Long eventId, Long couponId, Long memberId){
        LocalDateTime now = LocalDateTime.now();
        // 이벤트 및 쿠폰 검증
        EventCache eventCache = eventCacheOutPort.getEventCache(eventId);
        validateEvent(eventCache,now);

        // 쿠폰 시간 검증
        CouponCache couponCache = couponCacheOutPort.getCouponCache(couponId);
        validateCoupon(couponCache, now.toLocalDate());

        //재고처리 & 중복체크
        issueCouponWithStockCheck(memberId, couponId);

        redissonLockManager.saveIssuedCouponWithLock(couponId, memberId, now);
    }

    private void validateEvent(EventCache eventCache, LocalDateTime now){
        if (eventCache == null) {
            throw new BusinessException(CommonErrorCode.EVENT_NOT_FOUND);
        }
        if(!eventCache.isValid(now)){
            throw new BusinessException(CommonErrorCode.EVENT_NOT_ACTIVE);  // 이벤트 활성화되지 않음
        }
    }

    private void validateCoupon(CouponCache couponCache, LocalDate now) {
        if (couponCache == null) {
            throw new BusinessException(CommonErrorCode.COUPON_NOT_FOUND);
        }
        if (!couponCache.isValid(now)) {
            throw new BusinessException(CommonErrorCode.COUPON_NOT_ACTIVE);  // 쿠폰이 활성화되지 않음
        }
    }


    private void issueCouponWithStockCheck(Long memberId, Long couponId) {
        Long result = (Long) couponCacheOutPort.checkStockAndIssueCoupon(memberId, couponId);

        if (result == null) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);  // Redis 스크립트 실행 중 오류 발생
        } else if (result == 0) {
            throw new BusinessException(CommonErrorCode.COUPON_OUT_OF_STOCK);  // 재고 부족
        } else if (result == 2) {
            throw new BusinessException(CommonErrorCode.COUPON_ALREADY_ISSUED);  // 이미 발급된 경우
        }
    }

}
