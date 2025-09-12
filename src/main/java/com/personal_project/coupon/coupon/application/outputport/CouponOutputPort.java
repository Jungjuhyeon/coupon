package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.model.Coupon;

import java.util.Optional;

public interface CouponOutputPort {

    Optional<Coupon> findById(Long couponId);
    Coupon save(Coupon coupon);


}
