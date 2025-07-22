package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutputPort;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueAdapter implements CouponIssueOutputPort {

    private final CouponIssueJpaRepository couponIssueJpaRepository;
//    @Override
//    public boolean existByUserIdAndCouponId(Long memberId,Long couponId){
//        return couponIssueJpaRepository.existsByMemberIdAndCouponId(memberId,couponId);
//    }

    @Override
    public CouponIssue save(CouponIssue couponIssue){
        return couponIssueJpaRepository.save(couponIssue);
    }


}
