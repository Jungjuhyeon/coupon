package com.example.couponserver.coupon.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.CouponIssueOutputPort;
import com.example.couponserver.coupon.application.usecase.CouponIssueMakeUsedUseCase;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponIssueMakeUsedInputPort implements CouponIssueMakeUsedUseCase {
    private final CouponIssueOutputPort couponIssueOutputPort;

    @Override
    public void used(Long couponIssueId) {
        int updated = couponIssueOutputPort.useCoupon(couponIssueId);

        if (updated == 0) {
            throw new BusinessException(CouponErrorCode.COUPON_ALREADY_USED);
        }
    }
}
