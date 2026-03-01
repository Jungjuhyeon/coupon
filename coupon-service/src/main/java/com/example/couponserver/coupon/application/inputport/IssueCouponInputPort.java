package com.example.couponserver.coupon.application.inputport;

import com.example.couponserver.coupon.application.service.CouponIssueEventPublisher;
import com.example.couponserver.coupon.application.service.CouponIssuer;
import com.example.couponserver.coupon.application.service.CouponValidator;
import com.example.couponserver.coupon.application.service.PromotionValidator;
import com.example.couponserver.coupon.application.usecase.IssueCouponUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class IssueCouponInputPort implements IssueCouponUseCase {

    private final PromotionValidator promotionValidator;
    private final CouponValidator couponValidator;
    private final CouponIssuer couponIssuer;
    private final CouponIssueEventPublisher couponIssueEventPublisher;

    @Override
    public void issue(Long promotionId, Long couponId, Long memberId){
        LocalDateTime curTime = LocalDateTime.now();
        // 이벤트 기간 검증
        promotionValidator.validate(memberId, couponId, promotionId, curTime);
        // 쿠폰 기간 검증
        LocalDate endDate = couponValidator.validate(memberId, couponId, curTime);
        // 재고 검증 및 중복 검증 쿠폰 재고 감소
        couponIssuer.issue(memberId, couponId, curTime, endDate);
        //이벤트 처리
        couponIssueEventPublisher.publishEvent(memberId, couponId, curTime);
        couponIssueEventPublisher.publishSuccessLog(memberId,couponId,curTime);
    }

}
