package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.Coupon;

import java.util.Optional;

public interface CouponOutPort {

    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);

    Optional<Coupon> findByIdWithLock(Long couponId);

    void getLock(String key);

    void releaseLock(String key);


}
