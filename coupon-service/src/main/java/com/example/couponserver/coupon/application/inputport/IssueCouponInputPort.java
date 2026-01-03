package com.example.couponserver.coupon.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.EventOutputPort;
import com.example.couponserver.coupon.application.outputport.PromotionCacheOutputPort;
import com.example.couponserver.coupon.application.usecase.IssueCouponUsecase;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
import com.example.couponserver.coupon.domain.model.cache.PromotionCache;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;
import com.example.couponserver.coupon.domain.model.event.EventType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        validateEvent(promotionCache, curTime, couponId, memberId);

        // 쿠폰 시간 검증
        CouponCache couponCache = couponCacheOutputPort.getCouponCache(couponId);
        validateCoupon(couponCache, curTime, memberId, couponId);

        //재고처리 & 중복체크
        issueCouponWithStockCheck(curTime, memberId, couponId);

        CouponIssuedEvent couponIssuedEvent = CouponIssue.createCouponIssueEvent(couponId, memberId, curTime);
        //이벤트 처리
        eventOutputPort.occurCouponIssuedEvent(couponIssuedEvent);
        sendLogEvent(memberId,couponId,curTime, EventType.SUCCESS); //성공로그 이벤트

    }

    //실패 이벤트 발행 전용 메소드
    private void sendLogEvent(Long memberId, Long couponId, LocalDateTime time, EventType eventType) throws JsonProcessingException {
        CouponIssuedLogEvent couponIssuedLogEvent = CouponIssueLog.createCouponIssuedLogEvent(memberId,couponId,time,eventType);
        eventOutputPort.occurCouponIssuedLogEvent(couponIssuedLogEvent);
    }

    private void validateEvent(PromotionCache promotionCache, LocalDateTime now,Long memberId, Long couponId) throws JsonProcessingException {
        if (promotionCache == null) {
            throw new BusinessException(CouponErrorCode.PROMOTION_NOT_FOUND);
        }
        if(!promotionCache.isValid(now)){
            sendLogEvent(memberId,couponId,now,EventType.INVALID_TIME); //실패로그 이벤트
            throw new BusinessException(CouponErrorCode.PROMOTION_NOT_ACTIVE); // 이벤트 활성화되지 않음
        }
    }

    private void validateCoupon(CouponCache couponCache, LocalDateTime now, Long memberId, Long couponId) throws JsonProcessingException {
        if (couponCache == null) {
            throw new BusinessException(CouponErrorCode.COUPON_NOT_FOUND);
        }
        if (!couponCache.isValid(now.toLocalDate())) {
            sendLogEvent(memberId, couponId, now, EventType.INVALID_TIME); //실패로그 이벤트
            throw new BusinessException(CouponErrorCode.COUPON_NOT_ACTIVE);  // 쿠폰이 활성화되지 않음
        }
    }


    private void issueCouponWithStockCheck(LocalDateTime now,Long memberId, Long couponId) throws JsonProcessingException {
        Long result = (Long) couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId);

        if (result == null) {
            throw new BusinessException(CommonErrorCode.REDIS_SCRIPT_ERROR);  // Redis 스크립트 실행 중 오류 발생
        }
        else if (result == 0) {
            sendLogEvent(memberId, couponId, now, EventType.OUT_OF_STOCK); //실패로그 이벤트
            throw new BusinessException(CouponErrorCode.COUPON_OUT_OF_STOCK);  // 재고 부족
        }
        else if (result == 2) {
            System.out.println("이미 발급된 아이디 : "+ memberId);
            sendLogEvent(memberId, couponId, now, EventType.DUPLICATE); //실패로그 이벤트
            throw new BusinessException(CouponErrorCode.COUPON_ALREADY_ISSUED);  // 이미 발급된 경우
        }
    }

}
