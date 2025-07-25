package com.personal_project.coupon.coupon.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.application.outputport.CouponCacheOutputPort;
import com.personal_project.coupon.coupon.application.outputport.EventOutputPort;
import com.personal_project.coupon.coupon.application.outputport.PromotionCacheOutputPort;
import com.personal_project.coupon.coupon.application.usecase.IssueCouponUsecase;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import com.personal_project.coupon.coupon.domain.model.cache.CouponCache;
import com.personal_project.coupon.coupon.domain.model.cache.PromotionCache;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class IssueCouponInputPort implements IssueCouponUsecase {

    private final PromotionCacheOutputPort promotionCacheOutputPort;
    private final CouponCacheOutputPort couponCacheOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public void issue(Long promotionId, Long couponId, Long memberId) throws JsonProcessingException {
        LocalDateTime curTime = LocalDateTime.now();
        // 이벤트 및 쿠폰 검증
        PromotionCache promotionCache = promotionCacheOutputPort.getPromotionCache(promotionId);
        validateEvent(promotionCache,curTime);

        // 쿠폰 시간 검증
        CouponCache couponCache = couponCacheOutputPort.getCouponCache(couponId);
        validateCoupon(couponCache, curTime.toLocalDate());

        //재고처리 & 중복체크
        issueCouponWithStockCheck(memberId, couponId);

        CouponIssuedEvent couponIssuedEvent = CouponIssue.createCouponIssueEvent(couponId, memberId, curTime);
        //이벤트 처리
        eventOutputPort.occurCouponIssuedEvent(couponIssuedEvent);

    }

    private void validateEvent(PromotionCache promotionCache, LocalDateTime now){
        if (promotionCache == null) {
            throw new BusinessException(CommonErrorCode.PROMOTION_NOT_FOUND);
        }
        if(!promotionCache.isValid(now)){
            throw new BusinessException(CommonErrorCode.PROMOTION_NOT_ACTIVE);  // 이벤트 활성화되지 않음
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
        Long result = (Long) couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId);

        if (result == null) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);  // Redis 스크립트 실행 중 오류 발생
        } else if (result == 0) {
            throw new BusinessException(CommonErrorCode.COUPON_OUT_OF_STOCK);  // 재고 부족
        } else if (result == 2) {
            System.out.println("이미 발급된 아이디 : "+ memberId);
            throw new BusinessException(CommonErrorCode.COUPON_ALREADY_ISSUED);  // 이미 발급된 경우
        }
    }

}
