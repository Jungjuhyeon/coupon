package com.example.couponserver.coupon.application.usecase;

import com.example.couponserver.coupon.domain.model.Coupon;

public interface InquiryCouponUseCase {
    Coupon getCouponById(Long couponId);
}
