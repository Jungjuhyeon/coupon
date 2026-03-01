package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public List<Long> findActiveCoupons(LocalDate curDate) {
        return couponJpaRepository.findActiveCoupons(curDate);
    }

}
