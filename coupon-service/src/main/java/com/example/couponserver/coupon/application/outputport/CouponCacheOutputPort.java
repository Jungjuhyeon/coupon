package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.cache.CouponCache;

import java.time.LocalDate;

public interface CouponCacheOutputPort {
    void saveCouponData(Long couponId, int stock, LocalDate startDate, LocalDate endDate);
    CouponCache getCouponCache(Long couponId);
    Object checkStockAndIssueCoupon(Long memberId, Long couponId);
}
