package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.Coupon;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponOutputPort {
    Optional<Coupon> findById(Long couponId);
    Coupon save(Coupon coupon);

    List<Long> findActiveCoupons(LocalDate curDate);
}
