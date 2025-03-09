package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponAdapter implements CouponOutPort {

    private final CouponRepository couponRepository;
    @Override
    public Optional<Coupon> findById(Long couponId){
        return couponRepository.findById(couponId);
    }

}
