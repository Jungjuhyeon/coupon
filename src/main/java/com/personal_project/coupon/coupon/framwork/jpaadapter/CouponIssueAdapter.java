package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutPort;
import com.personal_project.coupon.coupon.domain.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueAdapter implements CouponIssueOutPort {

    private final CouponIssueJpaRepository couponIssueJpaRepository;
    @Override
    public boolean existByUserIdAndCouponId(Long memberId,Long couponId){
        return couponIssueJpaRepository.existsByMemberIdAndCouponId(memberId,couponId);
    }

    @Override
    public CouponIssue save(CouponIssue couponIssue){
        return couponIssueJpaRepository.save(couponIssue);
    }


}
