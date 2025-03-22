package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.domain.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findById(Long couponId);
}
