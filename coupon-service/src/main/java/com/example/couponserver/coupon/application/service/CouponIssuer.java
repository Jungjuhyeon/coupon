package com.example.couponserver.coupon.application.service;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.EventOutputPort;
import com.example.couponserver.coupon.domain.model.event.EventType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssuer {
    private final CouponCacheOutputPort couponCacheOutputPort;
    private final CouponIssueEventPublisher eventPublisher;

    public void issue(Long memberId, Long couponId, LocalDateTime now){
        Long result;

        try {
            result = (Long) couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }

        if (result == null) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }
        if (result == 0) {
            eventPublisher.publishFail(memberId, couponId, now, EventType.OUT_OF_STOCK);
            throw new BusinessException(CouponErrorCode.COUPON_OUT_OF_STOCK);
        }
        if (result == 2) {
            eventPublisher.publishFail(memberId, couponId, now, EventType.DUPLICATE);
            throw new BusinessException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }
}
