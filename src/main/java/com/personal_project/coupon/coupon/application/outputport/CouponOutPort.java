package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.entity.Coupon;

import java.util.Optional;

public interface CouponOutPort {

    Optional<Coupon> findById(Long couponId);
    Optional<Coupon> findByIdWithLock(Long couponId);
    Coupon save(Coupon coupon);

    void getLock(String key);

    void releaseLock(String key);



}
