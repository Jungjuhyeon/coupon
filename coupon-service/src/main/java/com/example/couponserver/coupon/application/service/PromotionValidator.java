package com.example.couponserver.coupon.application.service;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.PromotionCacheOutputPort;
import com.example.couponserver.coupon.domain.model.cache.PromotionCache;
import com.example.couponserver.coupon.domain.model.event.EventType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromotionValidator {
    private final PromotionCacheOutputPort promotionCacheOutputPort;
    private final CouponIssueEventPublisher couponIssueEventPublisher;

    public void validate(Long memberId, Long couponId, Long promotionId, LocalDateTime now){
        PromotionCache promotion = promotionCacheOutputPort.getPromotionCache(promotionId);

        try {
            if (promotion == null) {
                throw new BusinessException(CouponErrorCode.PROMOTION_NOT_FOUND);
            }
            if (!promotion.isValid(now)) {
                couponIssueEventPublisher.publishFail(memberId, couponId, now, EventType.INVALID_TIME);
                throw new BusinessException(CouponErrorCode.PROMOTION_NOT_ACTIVE);
            }
        }catch (Exception e) {
            // Redis 호출 실패 등
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);
        }
    }
}
