package com.example.couponserver.coupon.application.service;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
import com.example.couponserver.coupon.domain.model.event.EventType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponValidator {
    private final CouponCacheOutputPort couponCacheOutputPort;
    private final CouponIssueEventPublisher couponIssueEventPublisher;

    public void validate(Long memberId, Long couponId, LocalDateTime now){
        CouponCache coupon = couponCacheOutputPort.getCouponCache(couponId);

        try{
            if (coupon == null) {
                throw new BusinessException(CouponErrorCode.COUPON_NOT_FOUND);
            }
            if (!coupon.isValid(now.toLocalDate())) {
                couponIssueEventPublisher.publishFail(memberId, couponId, now, EventType.INVALID_TIME);
                throw new BusinessException(CouponErrorCode.COUPON_NOT_ACTIVE);
            }
        }catch (Exception e) {
            // Redis 호출 실패 등
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }
    }
}
