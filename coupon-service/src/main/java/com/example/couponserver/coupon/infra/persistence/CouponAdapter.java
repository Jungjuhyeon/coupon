package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponAdapter implements CouponOutputPort {

    private final CouponJpaRepository couponJpaRepository;
    @Override
    public Optional<Coupon> findById(Long couponId){
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon){
        return couponJpaRepository.save(coupon);
    }



}
