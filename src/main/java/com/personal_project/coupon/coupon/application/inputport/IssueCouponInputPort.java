package com.personal_project.coupon.coupon.application.inputport;

import com.personal_project.coupon.coupon.application.usecase.IssueCouponUsecase;
import com.personal_project.coupon.coupon.application.usecase.CouponIssueFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class IssueCouponInputPort implements IssueCouponUsecase {

    private final CouponIssueFacade couponIssueFacade;

    @Autowired
    public IssueCouponInputPort(@Qualifier("redissonLockCouponIssue") CouponIssueFacade couponIssueFacade) {
        this.couponIssueFacade = couponIssueFacade;
    }

    @Override
    public void issue(Long eventId, Long couponId, Long memberId) {
        // 로직에 따라 락을 다르게 선택
        couponIssueFacade.issueCoupon(eventId, couponId, memberId);
    }

}
