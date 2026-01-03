package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponJpaRepository extends JpaRepository<Coupon,Long> {

}
