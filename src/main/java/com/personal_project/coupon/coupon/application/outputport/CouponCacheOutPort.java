package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.CouponCache;

import java.time.LocalDate;

public interface CouponCacheOutPort {
    void saveCouponData(Long couponId, int stock, LocalDate startDate, LocalDate endDate);
    CouponCache getCouponCache(Long couponId);
    Object checkStockAndIssueCoupon(Long memberId, Long couponId);
}
