package com.example.couponserver.coupon.application.service;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.domain.model.event.EventType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssuer {
    private final CouponCacheOutputPort couponCacheOutputPort;
    private final CouponIssueEventPublisher eventPublisher;

    public void issue(Long memberId, Long couponId, LocalDateTime now, LocalDate endDate){
        Long result;

        try {
            result = (Long) couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId, now, endDate);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }

        if (result == null) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }
        if (result == 0) {
            eventPublisher.publishFailLog(memberId, couponId, now, EventType.OUT_OF_STOCK);
            throw new BusinessException(CouponErrorCode.COUPON_OUT_OF_STOCK);
        }
        if (result == 2) {
            eventPublisher.publishFailLog(memberId, couponId, now, EventType.DUPLICATE);
            throw new BusinessException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }
}
