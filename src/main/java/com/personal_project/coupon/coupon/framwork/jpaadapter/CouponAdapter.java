package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponAdapter implements CouponOutPort {

    private final CouponRepository couponRepository;
    private final LockJpaRepository lockJpaRepository;
    @Override
    public Optional<Coupon> findById(Long couponId){
        return couponRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon){
        return couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findByIdWithLock(Long couponId){
        return couponRepository.findByIdWithLock(couponId);
    }


    @Override
    public void getLock(String key){
        lockJpaRepository.getLock(key);
    }

    @Override
    public void releaseLock(String key){
        lockJpaRepository.releaseLock(key);
    }

}
