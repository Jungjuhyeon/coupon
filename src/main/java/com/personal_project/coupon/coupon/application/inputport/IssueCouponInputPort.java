package com.personal_project.coupon.coupon.application.inputport;

import com.personal_project.coupon.coupon.application.usercase.CouponIssue;
import com.personal_project.coupon.coupon.application.usercase.CouponIssueFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Qualifier("PessimisticLockCouponIssue")
public class IssueCouponInputPort implements CouponIssue {

    private final CouponIssueFacade couponIssueFacade;

    @Override
    @Transactional
    public void issue(Long eventId, Long couponId, Long memberId) {
        // 로직에 따라 락을 다르게 선택
        couponIssueFacade.issueCoupon(eventId, couponId, memberId);
    }

}
