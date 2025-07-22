package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponOutputPort;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponAdapter implements CouponOutputPort {

    private final CouponRepository couponRepository;
    @Override
    public Optional<Coupon> findById(Long couponId){
        return couponRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon){
        return couponRepository.save(coupon);
    }



}
