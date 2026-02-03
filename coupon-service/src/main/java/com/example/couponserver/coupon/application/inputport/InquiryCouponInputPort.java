package com.example.couponserver.coupon.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.application.usecase.InquiryCouponUseCase;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryCouponInputPort implements InquiryCouponUseCase {
    private final CouponOutputPort couponOutputPort;
    @Override
    public Coupon getCouponById(Long couponId) {
        return couponOutputPort.findById(couponId)
                .orElseThrow(() -> new BusinessException(CouponErrorCode.COUPON_NOT_FOUND));
    }
}
