package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.cache.CouponCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CouponCacheOutputPort {
    void saveCouponData(Long couponId, int stock, LocalDate startDate, LocalDate endDate);
    CouponCache getCouponCache(Long couponId);
    Object checkStockAndIssueCoupon(Long memberId, Long couponId, LocalDateTime now, LocalDate endDate);
    List<Map.Entry<Object, Object>> getReadyToPublishCoupons(Long couponId);
    void remove(String key, String value);
}
